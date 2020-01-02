(ns brew-bot-ui.logging
  "Interface for environment-specific logging"
  (:require [brew-bot-ui.config :as config]
            [circleci.rollcage.core :as rollcage]
            [clojure.tools.logging :as log]))

(def roller
  (rollcage/client config/rollcage-token {:environment "production"}))

(defn decorate-log
  "Add default information to every log"
  [message]
  (str "Application: " (:app (config/app-info)) " "
       "Version: " (:version (config/app-info)) " "
       message))

(defn info!
  [message]
  (log/info message)
  (when (= :rollbar config/logger)
    (rollcage/info roller message)))

(defn warn!
  [message]
  (log/warn message)
  (when (= :rollbar config/logger)
    (rollcage/warning roller message)))

(defn error!
  [message]
  (log/error message)
  (when (= :rollbar config/logger)
    (rollcage/error roller message)))

(defn fatal!
  [message]
  (log/fatal message)
  (when (= :rollbar config/logger)
    (rollcage/critical roller message)))

(def info
  (comp info! decorate-log))

(def warn
  (comp warn! decorate-log))

(def error
  (comp error! decorate-log))

(def fatal
  (comp fatal! decorate-log))

(defn wrap-app-error-handling
  "Ensure any application level errors are appropriately logged"
  [func]
  (try
    (func)
    (catch Throwable t
      (.println System/err (decorate-log t))
      (error (decorate-log t))
      (throw t))))

(defmacro with-error-handling
  [& body]
  `(wrap-app-error-handling (fn [] ~@body)))
