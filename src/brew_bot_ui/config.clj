(ns brew-bot-ui.config
  (:require [nnichols.parse :as parse]
            [trptcolin.versioneer.core :as versioneer]))

(def app-info
  (memoize #(hash-map :app "brew-bot-ui"
                      :version (versioneer/get-version "brew-bot-ui" "brew-bot-ui"))))

(def environment
  (or (System/getenv "HEROKU_ENV")
      (System/getenv "ENV")
      "dev"))

(def port
  (or (parse/try-parse-int (System/getenv "PORT")) 8000))

(def database-url
  (System/getenv "DATABASE_URL"))

(def ^:const features
  {"dev"  {:route-logging true
           :force-ssl?    false
           :force-json?   true}
   "test" {:route-logging false
           :force-ssl?    true
           :force-json?   false}
   "prod" {:route-logging true
           :force-ssl?    true
           :force-json?   true}})

(def log-routes?
  (get-in features [environment :route-logging]))

(def force-ssl?
  (get-in features [environment :force-ssl?]))

(def force-json?
  (get-in features [environment :force-json?]))
