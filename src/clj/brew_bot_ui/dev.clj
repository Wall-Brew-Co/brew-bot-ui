(ns brew-bot-ui.dev
(:require [brew-bot-ui.logging :as log]
          [brew-bot-ui.main :as main]
          [brew-bot-ui.http.server :as server]
          [ring.middleware.reload :refer [wrap-reload]]))

(defn -main
  "Development-time function to kick off everything and clean up afterwards: enables hot-reloading"
  [& args]
  (log/init-fallback-logger!)
  (log/with-error-handling (main/start-app args (wrap-reload #'server/app {:dirs ["src" "resources"]}))))
