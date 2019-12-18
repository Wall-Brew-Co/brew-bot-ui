(ns brew-bot-ui.config)

(def environment
  (or (System/getenv "HEROKU_ENV") "dev"))

(def port
  (or (System/getenv "PORT") "8000"))

(def ^:const features
  {"dev"  {:route-logging true}
   "test" {:route-logging false}
   "prod" {:route-logging true}})

(def log-routes?
  (get-in features [environment :route-logging]))
