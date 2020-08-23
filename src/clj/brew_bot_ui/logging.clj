(ns brew-bot-ui.logging
  "Interface for environment-specific logging"
  (:require [brew-bot-ui.config :as config]
            [circleci.rollcage.core :as rollcage]
            [clj-time.core :as time]
            [clojure.tools.logging :as log]))

(def roller
  (rollcage/client config/rollcage-token {:environment "production"}))

(defn decorate-log
  "Add default information to every log"
  [message]
  (str "Time: " (time/now) " "
       "Application: " (:app (config/app-info)) " "
       "Version: " (:version (config/app-info)) " "
       "Message: " message))

(defn info!
  [message]
  (log/info message))

(defn warn!
  [message]
  (log/warn message)
  (when (= :rollbar config/logger)
    (rollcage/warning roller (throw (Exception. message)))))

(defn error!
  [message]
  (log/error message)
  (when (= :rollbar config/logger)
    (rollcage/error roller (throw (Exception. message)))))

(defn fatal!
  [message]
  (log/fatal message)
  (when (= :rollbar config/logger)
    (rollcage/critical roller (throw (Exception. message)))))

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
      (error t)
      (throw t))))

(defmacro with-error-handling
  [& body]
  `(wrap-app-error-handling (fn [] ~@body)))

(defn init-fallback-logger
  "Start the rollcage fallback exception handler"
  []
  (rollcage/setup-uncaught-exception-handler roller))
