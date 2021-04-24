(ns brew-bot-ui.recipe-builder.events
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.shared.ingredient-utils :as util]
            [common-beer-format.data.data :as ingredient-data]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :initialize-db
 (fn [_ [_]]
   {:dispatch [:stay-alive]
    :db       db/default-db}))

(rf/reg-event-db
 :update-fermentable
 (fn [db [_ {:keys [fermentable-key amount]}]]
   (let [current-state (get-in db [:recipe :fermentables fermentable-key])]
     (if current-state
       (update-in db [:recipe :fermentables fermentable-key :amount] + amount)
       (let [base-fermentable (get ingredient-data/all-fermentables fermentable-key)
             fermentable (assoc base-fermentable :amount amount)]
         (assoc-in db [:recipe :fermentables fermentable-key] fermentable))))))

(rf/reg-event-db
 :update-hop
 (fn [db [_ {:keys [hop-key amount]}]]
   (let [current-state (get-in db [:recipe :hops hop-key])]
     (if current-state
       (update-in db [:recipe :hops hop-key :amount] + amount)
       (let [base-hop (get ingredient-data/all-hops hop-key)
             hop (assoc base-hop :amount amount)]
         (assoc-in db [:recipe :hops hop-key] hop))))))

(rf/reg-event-db
 :update-yeast
 (fn [db [_ {:keys [yeast-key amount]}]]
   (let [current-state (get-in db [:recipe :yeasts yeast-key])]
     (if current-state
       (update-in db [:recipe :yeasts yeast-key :amount] + amount)
       (let [base-yeast (get ingredient-data/all-yeasts yeast-key)
             yeast (assoc base-yeast :amount amount)]
         (assoc-in db [:recipe :yeasts yeast-key] yeast))))))

(rf/reg-event-db
   :fermentable-search
   (fn [db [_ search-criteria]]
     (let [matching-fermentables (util/search-ingredients :fermentables search-criteria)]
       (-> db
          (assoc-in [:search-boxes :fermentables] search-criteria)
          (assoc-in [:search-results :fermentables] matching-fermentables)))))
