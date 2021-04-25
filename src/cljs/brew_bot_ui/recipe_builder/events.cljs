(ns brew-bot-ui.recipe-builder.events
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.utils.ingredients :as util]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :initialize-db
 (fn [_ [_]]
   {:dispatch [:stay-alive]
    :db       db/default-db}))

(defn update-ingredient
  [db ingredient-type ingredient-key amount]
  (let [current-state (get-in db [:recipe ingredient-type ingredient-key])]
     (if current-state
       (update-in db [:recipe ingredient-type ingredient-key :amount] + amount)
       (let [base-ingredient (util/get-ingredient ingredient-type ingredient-key)
             new-ingredient (assoc base-ingredient :amount amount)]
         (assoc-in db [:recipe ingredient-type ingredient-key] new-ingredient)))))

(rf/reg-event-db
 :update-ingredient
 (fn [db [_ [_ ingredient-type ingredient-key amount]]]
   (update-ingredient db ingredient-type ingredient-key amount)))

(defn ingredient-search
  [db ingredient-type search-key search-criteria]
  (let [search-db            (assoc-in db [:search-boxes ingredient-type search-key] search-criteria)
        search-data          (get-in search-db [:search-boxes ingredient-type])
        matching-ingredients (util/search-ingredients ingredient-type search-data)]
    (assoc-in search-db [:search-results ingredient-type] matching-ingredients)))

(rf/reg-event-db
 :ingredient-search
 (fn [db [_ ingredient-type search-key search-criteria]]
   (ingredient-search db ingredient-type search-key search-criteria)))
