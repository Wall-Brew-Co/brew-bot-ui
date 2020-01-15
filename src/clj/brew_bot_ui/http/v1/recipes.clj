(ns brew-bot-ui.http.v1.recipes
  (:require [brew-bot-ui.core :as core]
            [brew-bot-ui.logging :as log]
            [compojure.coercions :refer [as-uuid]]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [nnichols.http :as nhttp]
            [ring.util.response :as resp]))

(defroutes routes
  (GET "/v1/recipes/:id"
    [id :<< as-uuid]
    (if-let [recipe (core/get-recipe id)]
      (resp/response recipe)
      {:status 404})))
