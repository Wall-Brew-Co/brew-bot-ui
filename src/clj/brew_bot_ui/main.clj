(ns brew-bot-ui.main
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.logging :as log]
            [brew-bot-ui.http.server :as server]
            [clojure.tools.cli :as cli]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn start-app
  [args app]
  (let [[params] (cli/cli args ["-p" "--port" "Listen on this port" :default 8080 :parse-fn #(Integer/parseInt %)])
        port     (or @config/port (:port params))]
    (jetty/run-jetty app {:port port})))

(defn -main
  [& args]
  (log/init-fallback-logger!)
  (log/with-error-handling (start-app args server/app)))
