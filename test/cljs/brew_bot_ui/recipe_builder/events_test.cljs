(ns brew-bot-ui.recipe-builder.events-test
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [brew-bot-ui.recipe-builder.events :as sut]
            [brew-bot-ui.utils.ingredients :as util]
            [cljs.test :refer-macros [is deftest]]
            [clojure.spec.alpha :as csa]
            [common-beer-format.specs.fermentables :as ferms]
            [common-beer-format.specs.hops :as hops]
            [common-beer-format.specs.yeasts :as yeasts]))

(deftest update-ingredient-test
  (let [blank-db-update (sut/update-ingredient db/default-db :fermentables :barley-hulls 1.25)]
    (is (csa/valid? ::ferms/fermentable (get-in blank-db-update [:recipe :fermentables :barley-hulls])))
    (is (= 1 (count (keys (get-in blank-db-update [:recipe :fermentables])))))
    (is (= 1.25 (get-in blank-db-update [:recipe :fermentables :barley-hulls :amount])))
    (is (empty? (get-in blank-db-update [:recipe :hops])))
    (is (empty? (get-in blank-db-update [:recipe :yeasts])))
    (let [updated-ingredient (sut/update-ingredient blank-db-update :fermentables :barley-hulls 0.75)]
      (is (csa/valid? ::ferms/fermentable (get-in updated-ingredient [:recipe :fermentables :barley-hulls])))
      (is (= 1 (count (keys (get-in updated-ingredient [:recipe :fermentables])))))
      (is (= 2.0 (get-in updated-ingredient [:recipe :fermentables :barley-hulls :amount])))
      (is (empty? (get-in updated-ingredient [:recipe :hops])))
      (is (empty? (get-in updated-ingredient [:recipe :yeasts])))
      (let [new-hop (sut/update-ingredient updated-ingredient :hops :el-dorado 0.5)]
        (is (csa/valid? ::ferms/fermentable (get-in new-hop [:recipe :fermentables :barley-hulls])))
        (is (= 1 (count (keys (get-in new-hop [:recipe :fermentables])))))
        (is (= 2.0 (get-in new-hop [:recipe :fermentables :barley-hulls :amount])))
        (is (csa/valid? ::hops/hop (get-in new-hop [:recipe :hops :el-dorado])))
        (is (= 1 (count (keys (get-in new-hop [:recipe :hops])))))
        (is (= 0.5 (get-in new-hop [:recipe :hops :el-dorado :amount])))
        (is (empty? (get-in updated-ingredient [:recipe :yeasts])))))))

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
