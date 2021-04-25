(ns brew-bot-ui.utils.ingredients
  (:require [nnichols.string :as nns]
            [nnichols.util :as nnu]
            [common-beer-format.data.data :as ingredient-data]))

(defn all-ingredients-by-type
  [ingredient-type]
  (case ingredient-type
    :fermentables ingredient-data/all-fermentables
    :hops         ingredient-data/all-hops
    :yeasts       ingredient-data/all-yeasts))

(defn ingredient-type->ingredient-list
  [ingredient-type]
  (vals (all-ingredients-by-type ingredient-type)))

(defn ingredient-matches-query?
  [search-criteria ingredient]
  (nns/string-includes? (:name ingredient) (:name search-criteria)))

(defn search-ingredients
  [ingredient-type search-criteria]
  (let [ingredients (ingredient-type->ingredient-list ingredient-type)
        filter-fn   (partial ingredient-matches-query? search-criteria)]
    (filter filter-fn ingredients)))

(defn get-ingredient
  [ingredient-type ingredient-key]
  (get (all-ingredients-by-type ingredient-type) ingredient-key :no-match))

(defn get-ingredient-by-name
  [ingredient-type ingredient-name]
  (let [ingredients (all-ingredients-by-type ingredient-type)]
    (nnu/filter-by-values #(nns/string-includes? (:name %) ingredient-name) ingredients)))
