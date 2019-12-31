(ns brew-bot-ui.db.connection
  "Database connection pooling + convenience functions"
  (:require [brew-bot-ui.config :as config]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as conn]
            [next.jdbc.result-set :as r-set])
  (:import (com.zaxxer.hikari HikariDataSource)))

(def db-spec
  {:maximumPoolSize 5
   :poolName        "bb-db-pool"
   :jdbcUrl         config/jdbc-url})

(def query-defaults
  {:builder-fn r-set/as-unqualified-lower-maps})

(defn execute!
  "Wrap JDBC's `execute!` to simplify using the connection pool"
  ([query]
   (execute! query query-defaults))

  ([query opts]
   (with-open [^HikariDataSource data-source (conn/->pool HikariDataSource db-spec)]
     (jdbc/execute! data-source query (merge query-defaults opts)))))
