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
  (java.net.URI. (or (System/getenv "DATABASE_URL")
                     "postgresql://localhost:5432/brew-bot")))

(def jdbc-url
  (or (System/getenv "JDBC_DATABASE_URL")
      "jdbc:postgresql://localhost:5432/brew-bot"))

(def database-name
  (System/getenv "DATABASE_NAME"))

(def rollcage-token
  (System/getenv "ROLLBAR_ACCESS_TOKEN"))

(def database-config
  {:adapter       "postgresql"
   :database-name database-name
   :pool-name     "brew-bot-cp"
   :server-name   database-url})

(def ^:const features
  {"dev"  {:route-logging true
           :debug-routes? true
           :logger        :clojure
           :force-ssl?    false
           :force-json?   true}
   "test" {:route-logging false
           :debug-routes? true
           :logger        :clojure
           :force-ssl?    true
           :force-json?   false}
   "prod" {:route-logging true
           :debug-routes? false
           :logger        :rollbar
           :force-ssl?    true
           :force-json?   true}})

(def log-routes?
  (get-in features [environment :route-logging]))

(def debug-routes?
  (get-in features [environment :debug-routes?]))

(def logger
  (get-in features [environment :logger]))

(def force-ssl?
  (get-in features [environment :force-ssl?]))

(def force-json?
  (get-in features [environment :force-json?]))
