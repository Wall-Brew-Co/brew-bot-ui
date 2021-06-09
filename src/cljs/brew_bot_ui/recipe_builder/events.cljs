(ns brew-bot-ui.recipe-builder.events
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.utils.ingredients :as util]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :initialize-db
 (fn [_ [_]]
   {:dispatch [:stay-alive]
    :db       db/default-db}))

(defn add-ingredient
  [db ingredient-type ingredient]
  (update-in db [:recipe ingredient-type] conj ingredient))

(rf/reg-event-db
 :add-ingredient
 (fn [db [_ ingredient-type ingredient]]
   (add-ingredient db ingredient-type ingredient)))

(defn delete-ingredient
  [db ingredient-type ingredient]
  (let [ingredients (get-in db [:recipe ingredient-type])
        new-ingredients (remove #(= ingredient %) ingredients)]
    (assoc-in db [:recipe ingredient-type] new-ingredients)))

(rf/reg-event-db
 :delete-ingredient
 (fn [db [_ ingredient-type ingredient]]
   (delete-ingredient db ingredient-type ingredient)))

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
