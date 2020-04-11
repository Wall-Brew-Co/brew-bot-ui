(ns brew-bot-ui.repl.db-migrations
  (:require [brew-bot-ui.config :as config]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]))

(comment
  (def db-config
    {:datastore  (jdbc/sql-database {:dbtype "postgresql"
                                     :dbname config/database-name
                                     :host config/database-url
                                     :ssl true
                                     :sslfactory "org.postgresql.ssl.NonValidatingFactory"}) ;; Add valid user data at runtime
     :migrations (jdbc/load-directory "deploy/sql/")})

  (defn migrate-up
    []
    (repl/migrate db-config))

  (defn migrate-down
    []
    (repl/migrate db-config)))
