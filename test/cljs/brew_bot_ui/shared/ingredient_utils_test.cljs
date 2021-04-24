(ns brew-bot-ui.shared.ingredient-utils-test
  (:require [brew-bot-ui.shared.ingredient-utils :as sut]
            [cljs.test :refer-macros [is deftest]]
            [clojure.spec.alpha :as csa]
            [common-beer-format.specs.fermentables :as ferms]
            [common-beer-format.specs.hops :as hops]
            [common-beer-format.specs.yeasts :as yeasts]))

(deftest all-ingredients-by-type-test
  (is (every? #(csa/valid? ::ferms/fermentable (sut/all-ingredients-by-type :fermentables))))
  (is (every? #(csa/valid? ::hops/hop (sut/all-ingredients-by-type :hops))))
  (is (every? #(csa/valid? ::yeasts/yeast (sut/all-ingredients-by-type :yeast)))))

(deftest ingredient-matches-query?-test
  (let [sample-ingredient (first (filter #(= (:name %) "Belgian Candi Syrup - 45L") (sut/all-ingredients-by-type :fermentables)))]
    (is (true? (sut/ingredient-matches-query? {:name "Belgian Candi Syrup - 45L "} sample-ingredient)))
    (is (true? (sut/ingredient-matches-query? {:name " belgian"} sample-ingredient)))
    (is (true? (sut/ingredient-matches-query? {:name " 45L "} sample-ingredient)))
    (is (false? (sut/ingredient-matches-query? {:name "Candy"} sample-ingredient)))
    (is (true? (sut/ingredient-matches-query? {:name ""} sample-ingredient)))))

(deftest search-ingredients-test
  (is (= 1 (count (sut/search-ingredients :fermentables {:name "Belgian Candi Syrup - 45L "}))))
  (is (seq (sut/search-ingredients :fermentables {:name " belgian"})))
  (is (= (sut/all-ingredients-by-type :fermentables) (sut/search-ingredients :fermentables {:name ""}))))
