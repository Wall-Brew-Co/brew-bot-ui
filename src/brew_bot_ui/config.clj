(ns brew-bot-ui.config)

(def environment
  (or (System/getenv "HEROKU_ENV") "dev"))

(def port
  (or (System/getenv "PORT") "8000"))
