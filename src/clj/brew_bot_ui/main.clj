(ns brew-bot-ui.main
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.logging :as log]
            [brew-bot-ui.http.server :as server]
            [clojure.tools.cli :refer [parse-opts]]
            [luminus.http-server :as http]
            [luminus.repl-server :as repl]
            [mount.core :as mount])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

(mount/defstate ^{:on-reload :noop} http-server
  :start
  (http/start {:port                config/port
               :request-header-size 65536 ; make sure this matches dev.cljs.edn
               :handler             #'server/app
               :io-threads          (* 2 (.availableProcessors (Runtime/getRuntime)))})
  :stop
  (http/stop http-server))

(mount/defstate ^{:on-reload :noop} repl-server
  :start
  (when-let [nrepl-port config/nrepl-port]
    (repl/start {:port nrepl-port}))
  :stop
  (when repl-server
    (repl/stop repl-server)))

(defn stop-app
  []
  (doseq [component (:stopped (mount/stop))]
    (log/info (str component " stopped")))
  (shutdown-agents))

(defn start-app
  [args]
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info (str component " started")))
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(stop-app))))

(defn -main
  [& args]
  (log/init-fallback-logger!)
  (log/with-error-handling (start-app args)))
