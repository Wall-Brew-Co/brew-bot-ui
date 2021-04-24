(ns brew-bot-ui.shared.ingredient-utils
  (:require [nnichols.string :as nns]
            [common-beer-format.data.data :as ingredient-data]))

(defn all-ingredients-by-type
  [ingredient-type]
  (case ingredient-type
    :fermentables (vals ingredient-data/all-fermentables)
    :hops         (vals ingredient-data/all-hops)
    :yeasts       (vals ingredient-data/all-yeasts)))

(defn ingredient-matches-query?
  [search-criteria ingredient]
  (nns/string-includes? (:name ingredient) (:name search-criteria)))

(defn search-ingredients
  [ingredient-type search-criteria]
  (let [ingredients (all-ingredients-by-type ingredient-type)
        filter-fn   (partial ingredient-matches-query? search-criteria)]
    (filter filter-fn ingredients)))
