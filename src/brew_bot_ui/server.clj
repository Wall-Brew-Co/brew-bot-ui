(ns brew-bot-ui.server
  (:require [brew-bot-ui.config :as config]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults secure-site-defaults]]
            [ring.logger :as logger]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Heroku"})

(defn bodiless-response
  "Creates a boddiless ring response with status `code`"
  [code]
  {:status code
   :headers {"Content-Type" "application/json; charset=UTF-8"}})

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
  [handler]
  (fn [request]
    (try (handler request)
         (catch Throwable t
           (log/error {:message "Uncaught handler error"
                       :x-session-id (get-in request [:headers "x-session-id"])
                       :error t})
           (bodiless-response 500)))))

(defroutes app-routes
           (GET "/" []
                (splash))
           (GET "/heartbeat" []
                (bodiless-response 200))
           (ANY "*" []
                (route/not-found (slurp (io/resource "404.html")))))

(def app
  (-> (wrap-defaults app-routes secure-site-defaults)
      wrap-ignore-trailing-slash
      wrap-internal-error
      logger/wrap-log-response
      logger/wrap-log-request-params))

(defn -main
  [& [port]]
  (let [port (Integer. (or port config/port 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
