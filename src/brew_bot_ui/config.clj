(ns brew-bot-ui.config
  (:require [clojure.core.memoize :as memo]))

(def environment
  (or (System/getenv "HEROKU_ENV") "dev"))

(def port
  (or (System/getenv "PORT") "8000"))
