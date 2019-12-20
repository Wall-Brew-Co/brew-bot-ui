(ns brew-bot-ui.server
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.middleware :as mw]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [nnichols.http :as nhttp]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as ring]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Heroku"})

(defroutes app-routes
           (GET "/" []
                (splash))
           (GET "/heartbeat" []
                (nhttp/bodiless-json-response 200))
           (GET "/info" []
             {:status 200 :body (config/app-info)})
           (ANY "*" []
                (route/not-found (slurp (io/resource "404.html")))))

(def app
  (-> (ring/wrap-defaults app-routes ring/secure-site-defaults)
      mw/wrap-ignore-trailing-slash
      mw/wrap-internal-error
      mw/wrap-logging))

(defn- wrap-app-error-handling
  "Ensure any application level errors are appropriately logged"
  [func]
  (try
    (func)
    (catch Throwable t
      (.println System/err (str "Error in brew-bot-ui: " t))
      (log/error t "Error in brew-bout-ui")
      (throw t))))

(defmacro with-error-handling
  [& body]
  `(wrap-app-error-handling (fn [] ~@body)))

(defn -main
  [& args]
  (with-error-handling (jetty/run-jetty (site #'app) {:port config/port :join? false})))
