(ns brew-bot-ui.middleware
  (:require [brew-bot-ui.config :as config]
            [clojure.tools.logging :as log]
            [compojure.handler :as handler]
            [nnichols.http :as http]
            [ring.logger :as logger]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.defaults :as ring]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.x-headers :refer [wrap-xss-protection]]))

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
           (log/error {:message "Uncaught handler error"
                       :x-session-id (get-in request [:headers "x-session-id"])
                       :error t})
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

(defn wrap-anti-forgery-check
  "Block all requests with invalid CSRF protection tokens"
  [handler]
  (wrap-anti-forgery handler {:error-response (http/bodiless-json-response 500)}))

(def ring-default-options
  "Update ring's default secure options to account for Heroku's balancers/test modes"
  (-> ring/secure-site-defaults
      (assoc :proxy true)
      (assoc-in [:security :ssl-redirect] config/force-ssl?)))

(defn wrap-json-conformer
  "Don't conform return values to JSON when testing for easier assertions"
  [handler]
  (fn [request]
    (let [wrapped-handler (wrap-json-response handler)]
      (if config/force-json?
        (wrapped-handler request)
        (handler request)))))

(defn wrap-base
  "The specialty handlers invoked for every HTTP(S) call"
  [handler]
  (-> handler
      wrap-json-conformer
      (wrap-xss-protection true {:mode :block})
      wrap-anti-forgery-check
      wrap-internal-error
      (ring/wrap-defaults ring-default-options)
      wrap-ignore-trailing-slash
      wrap-logging))
