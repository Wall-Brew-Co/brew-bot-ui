(ns brew-bot-ui.recipe-builder.events
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.shared.ingredient-utils :as util]
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

(rf/reg-event-db
   :ingredient-search
   (fn [db [_ ingredient-type search-criteria]]
     (let [matching-fermentables (util/search-ingredients ingredient-type search-criteria)]
       (-> db
          (assoc-in [:search-boxes ingredient-type] search-criteria)
          (assoc-in [:search-results ingredient-type] matching-fermentables)))))
