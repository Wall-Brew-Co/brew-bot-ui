(ns brew-bot-ui.recipe-builder.events-test
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.recipe-builder.events :as sut]
            [brew-bot-ui.utils.ingredients :as util]
            [cljs.test :refer-macros [is deftest]]
            [clojure.spec.alpha :as csa]
            [common-beer-format.specs.fermentables :as ferms]
            [common-beer-format.specs.hops :as hops]
            [common-beer-format.specs.yeasts :as yeasts]))

(deftest ingredient-search-test
  (let [blank-db-search (sut/ingredient-search db/default-db :yeasts :name "")]
    (is (empty? (get-in blank-db-search [:search-results :fermentables])))
    (is (empty? (get-in blank-db-search [:search-results :hops])))
    (is (= (util/ingredient-type->ingredient-list :yeasts) (get-in blank-db-search [:search-results :yeasts]))))
  (let [yeast-search (sut/ingredient-search db/default-db :yeasts :name "wlp")]
    (is (empty? (get-in yeast-search [:search-results :fermentables])))
    (is (empty? (get-in yeast-search [:search-results :hops])))
    (is (every? #(csa/valid? ::yeasts/yeast %) (get-in yeast-search [:search-results :yeasts])))
    (is (= 1 (count (filter #(= (:name %) "WLP011 European Ale") (get-in yeast-search [:search-results :yeasts])))))
    (is (= 0 (count (filter #(= (:name %) "K-97 SafAle German Ale") (get-in yeast-search [:search-results :yeasts])))))))
