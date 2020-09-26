(ns brew-bot-ui.db.queries
  (:require [brew-bot-ui.db.connection :as db]
            [cheshire.core :as json]
            [clj-time.core :as time]
            [brew-bot-ui.logging :as log]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]
            [honeysql-postgres.format] ;must be required for the extension to work
            [honeysql-postgres.helpers :as pghelpers]
            [nnichols.parse :as np]
            [nnichols.util :as nu]))

(defn get-all-recipes
  []
  (log/info "get-all-recipes")
  (let [q (sql/format (-> (helpers/select :*)
                          (helpers/from :beer_recipes)))]
    (db/execute! q)))

(defn get-recipe-by-id
  [recipe-id]
  (let [recipe-uuid (np/parse-uuid recipe-id)
        _ (log/info (str "get-recipe-by-id: " recipe-uuid))
        q (sql/format (-> (helpers/select :*)
                          (helpers/from :beer_recipes)
                          (helpers/where [:= :recipe_id recipe-uuid])))]
    (db/execute! q)))

(defn insert-recipe
  [recipe-data recipe-generator metadata]
  (let [recipe-json   (sql/call :jsonb (json/generate-string recipe-data))
        metadata-json (sql/call :jsonb (json/generate-string metadata))
        created-at    (time/now)
        recipe-id     (nu/uuid)
        _ (log/info (str "insert-recipe: " recipe-id))
        row [{:recipe_id      recipe-id
              :created_at     created-at
              :generator_type recipe-generator
              :recipe         recipe-json
              :metadata       metadata-json}]
        q (sql/format (-> (helpers/insert-into :beer_recipes)
                          (helpers/values row)))]
     (db/execute! q)))
