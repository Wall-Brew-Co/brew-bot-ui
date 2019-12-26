(ns brew-bot-ui.config
  (:require [nnichols.parse :as parse]
            [trptcolin.versioneer.core :as versioneer]))

(def app-info
  (memoize #(hash-map :app "brew-bot-ui"
                      :version (versioneer/get-version "brew-bot-ui" "brew-bot-ui"))))

(def environment
  (or (System/getenv "HEROKU_ENV") "dev"))

(def port
  (or (parse/try-parse-int (System/getenv "PORT")) 8000))

(def ^:const features
  {"dev"  {:route-logging true}
   "test" {:route-logging false}
   "prod" {:route-logging true}})

(def log-routes?
  (get-in features [environment :route-logging]))
