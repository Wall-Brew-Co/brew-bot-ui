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
            [ring.middleware.defaults :refer [wrap-defaults secure-site-defaults]]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from Heroku"})

(defroutes app-routes
           (GET "/" []
                (splash))
           (GET "/heartbeat" []
                (nhttp/bodiless-json-response 200))
           (ANY "*" []
                (route/not-found (slurp (io/resource "404.html")))))

(def app
  (-> (wrap-defaults app-routes secure-site-defaults)
      mw/wrap-ignore-trailing-slash
      mw/wrap-internal-error
      mw/wrap-logging))

(defn -main
  [& [port]]
  (let [port (Integer. (or port config/port 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
