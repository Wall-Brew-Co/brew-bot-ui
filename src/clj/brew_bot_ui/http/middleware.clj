(ns brew-bot-ui.http.middleware
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.http.layout :as layout]
            [brew-bot-ui.logging :as log]
            [nnichols.http :as http]
            [jumblerg.middleware.cors :refer [wrap-cors]]
            [ring.logger :as logger]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.defaults :as ring]
            [ring.middleware.gzip :as gzip]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.ssl :as ring-ssl]
            [ring-ttl-session.core :refer [ttl-memory-store]]
            [ring.util.response :as resp]))

(defn wrap-ignore-trailing-slash
  "Modifies the request uri before calling the handler.
   Removes a single trailing slash from the end of the uri if present.
   Related to: https://github.com/weavejester/compojure/wiki/Common-Problems"
  [handler]
  (fn [request]
    (let [uri (:uri request)
          trailing-slash? (and (not= "/" uri)
                               (.endsWith uri "/"))
          effective-uri (if trailing-slash?
                          (subs uri 0 (dec (count uri)))
                          uri)]
      (handler (assoc request :uri effective-uri)))))

(defn wrap-internal-error
  "Takes exceptions thrown by the handler and logs them without barfing them out"
  [handler]
  (fn [request]
    (try (handler request)
         (catch Throwable t
           (log/error t)
           (http/bodiless-json-response 500)))))

(defn wrap-logging
  "Silences route-level logging for test environments"
  [handler]
  (fn [request]
    (let [wrapped-handler (-> handler
                              logger/wrap-log-response
                              logger/wrap-log-request-params)]
      (if config/log-routes?
        (wrapped-handler request)
        (handler request)))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (layout/render "403.html")}))

(def default-ring-options
  "Update ring's default secure options to account for Heroku's balancers/test modes"
  (-> ring/secure-site-defaults
      (assoc :proxy true)
      (assoc-in [:security :anti-forgery] false) ;; This is handled elsewhere
      (assoc-in [:session :store] (ttl-memory-store (* 60 30)))
      (assoc-in [:security :ssl-redirect] config/force-ssl?)))

(defn wrap-ssl-redirect
  [handler]
  (if config/force-ssl?
    (-> handler
        (ring-ssl/wrap-ssl-redirect {:ssl-port 443})
        ring-ssl/wrap-forwarded-scheme)
    handler))

(defn wrap-json-conformer
  "Don't conform return values to JSON when testing for easier assertions"
  [handler]
  (fn [request]
    (let [wrapped-handler (wrap-json-response handler)]
      (if config/force-json?
        (wrapped-handler request)
        (handler request)))))

(defn wrap-no-cache
  "Takes a Ring response and returns the response with additional headers that instruct the client not to cache the response."
  [resp]
  (-> resp
      (resp/header "Cache-Control" "no-store, must-revalidate")
      (resp/header "Pragma" "no-cache")
      (resp/header "Expires" "0")))

(defn wrap-base
  "The specialty handlers invoked for every HTTP(S) call"
  [handler]
  (-> handler
      (wrap-cors #".*localhost.*" #".*wallbrew.*" #".*herokuapp.*")
      wrap-internal-error
      (ring/wrap-defaults default-ring-options)
      wrap-ssl-redirect
      wrap-ignore-trailing-slash
      wrap-csrf
      wrap-session
      wrap-logging
      wrap-json-conformer
      (wrap-json-body {:keywords? true})
      gzip/wrap-gzip))
