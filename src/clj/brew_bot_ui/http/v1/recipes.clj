(ns brew-bot-ui.http.v1.recipes
  (:require [brew-bot-ui.core :as core]
            [brew-bot-ui.http.route-specs :as rs]
            [brew-bot-ui.logging :as log]
            [compojure.coercions :refer [as-uuid]]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [nnichols.http :as nhttp]
            [ring.util.response :as resp]))

(defn get-v1-recipe-id
  [id]
  (if-let [recipe (core/get-recipe id)]
    (resp/response recipe)
    (nhttp/bodiless-json-response 404)))

(defn put-v1-recipe
  [recipe]
  (let [generator   (:generator recipe)
        metadata    (:metadata recipe)
        recipe-data (dissoc recipe :generator :metadata)]
    (if-let [added-recipe (core/add-recipe recipe-data generator metadata)]
      added-recipe
      (nhttp/bodiless-json-response 500))))

(defroutes routes
  (GET "/v1/recipes/:id"
    [id :<< as-uuid]
    (rs/conform-route ::rs/uuid id get-v1-recipe-id))

  (GET "/v1/recipes/recipe"
    {:keys [body]}
    (rs/conform-route ::rs/v1-recipe body put-v1-recipe)))
