(ns brew-bot-ui.main
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.logging :as log]
            [brew-bot-ui.http.server :as server]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn -main
  [& _args]
  (log/with-error-handling (jetty/run-jetty (site #'server/app) {:port config/port :join? false})))
