(ns brew-bot-ui.server
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.middleware :as mw]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [nnichols.http :as nhttp]
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
