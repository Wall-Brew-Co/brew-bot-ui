(ns brew-bot-ui.main
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.server :as server]
            [circleci.rollcage.core :as rollcage]
            [clojure.tools.logging :as log]
            [compojure.handler :refer [site]]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(def roller
  (rollcage/client config/rollcage-token {:environment "production"}))

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
  (rollcage/setup-uncaught-exception-handler roller)
  (with-error-handling (jetty/run-jetty (site #'server/app) {:port config/port :join? false})))
