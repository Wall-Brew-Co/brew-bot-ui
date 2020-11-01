(ns brew-bot-ui.logging
  "Interface for environment-specific logging"
  (:require [brew-bot-ui.config :as config]
            [circleci.rollcage.core :as rollcage]
            [taoensso.timbre :as log]
            [wb-metrics.logging :as metrics]))

(def roller
  (rollcage/client config/rollcage-token {:environment "production"}))

(defn info
  [message]
  (log/info message))

(defn warn
  [message]
  (log/warn message)
  (when (= :rollbar config/logger)
    (rollcage/warning roller (throw (Exception. message)))))

(defn error
  [message]
  (log/error message)
  (when (= :rollbar config/logger)
    (rollcage/error roller (throw (Exception. message)))))

(defn fatal
  [message]
  (log/fatal message)
  (when (= :rollbar config/logger)
    (rollcage/critical roller (throw (Exception. message)))))

(defn wrap-app-error-handling
  "Ensure any application level errors are appropriately logged"
  [func]
  (try
    (func)
    (catch Throwable t
      (error t)
      (throw t))))

(defmacro with-error-handling
  [& body]
  `(wrap-app-error-handling (fn [] ~@body)))

(defn init-fallback-logger!
  "Start the rollcage fallback exception handler and the wb-metrics reporter"
  []
  (metrics/configure! {:group-name "com.wallbrew" :artifact-name "brew-bot-ui"})
  (rollcage/setup-uncaught-exception-handler roller))
