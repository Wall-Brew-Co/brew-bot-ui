(ns brew-bot-ui.db
  (:require [brew-bot-ui.config :as config]
            [clojure.string :as cs]
            [clojure.tools.logging :as log]
            [honeysql.core :as hsql]
            [honeysql.helpers :as helpers]
            [honeysql-postgres.format] ;must be required for the extension to work
            [honeysql-postgres.helpers :as pghelpers]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as r-set]
            [nnichols.parse :as np])
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
   (with-open [^HikariDataSource data-source (connection/->pool HikariDataSource db-spec)]
     (jdbc/execute! data-source query (merge query-defaults opts)))))


(defn get-all-recipes
  []
  (let [q (hsql/format (-> (helpers/select :*)
                           (helpers/from :beer_recipes)))]
    (execute! q)))


(defn get-recipe-by-id
  [recipe-id]
  (let [recipe-uuid (np/parse-uuid recipe-id)
        q (hsql/format (-> (helpers/select :*)
                           (helpers/from :beer_recipes)
                           (helpers/where [:= :recipe_id recipe-uuid])))]
    (execute! q)))
