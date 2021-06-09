(ns brew-bot-ui.utils.ingredients-test
  (:require [brew-bot-ui.utils.ingredients :as sut]
            [clojure.spec.alpha :as csa]
            [common-beer-format.specs.fermentables :as ferms]
            [common-beer-format.specs.hops :as hops]
            [common-beer-format.specs.yeasts :as yeasts]
            #? (:clj  [clojure.test :refer [deftest is testing]])
            #? (:cljs [cljs.test    :refer-macros [deftest is testing]])))

(deftest ingredient-type->ingredient-list-test
  (testing "Loading ingredient types returns a sequence of ingredients"
    (is (every? #(csa/valid? ::ferms/fermentable %) (sut/ingredient-type->ingredient-list :fermentables)))
    (is (every? #(csa/valid? ::hops/hop %) (sut/ingredient-type->ingredient-list :hops)))
    (is (every? #(csa/valid? ::yeasts/yeast %) (sut/ingredient-type->ingredient-list :yeasts)))))

(deftest ingredient-matches-query?-test
  (testing "Ingredients can be matched by name, case-insensitive"
    (let [sample-ingredient (first (filter #(= (:name %) "Belgian Candi Syrup - 45L") (sut/ingredient-type->ingredient-list :fermentables)))]
      (is (true? (sut/ingredient-matches-query? {:name "Belgian Candi Syrup - 45L "} sample-ingredient)))
      (is (true? (sut/ingredient-matches-query? {:name " belgian"} sample-ingredient)))
      (is (true? (sut/ingredient-matches-query? {:name " 45L "} sample-ingredient)))
      (is (false? (sut/ingredient-matches-query? {:name "Candy"} sample-ingredient)))
      (is (true? (sut/ingredient-matches-query? {:name ""} sample-ingredient))))))

(deftest search-ingredients-test
  (testing "Ingredients can be queried by name, case-insensitive"
    (is (= 1 (count (sut/search-ingredients :fermentables {:name "Belgian Candi Syrup - 45L "}))))
    (is (seq (sut/search-ingredients :fermentables {:name " belgian"})))
    (is (= (sut/ingredient-type->ingredient-list :fermentables) (sut/search-ingredients :fermentables {:name ""})))))

(deftest get-ingredient-test
  (testing "Ingredients can be pulled by key"
    (is (= :no-match (sut/get-ingredient :hops :some-fake-key)))
    (is (csa/valid? ::ferms/fermentable (sut/get-ingredient :fermentables (rand-nth (keys (sut/all-ingredients-by-type :fermentables))))))
    (is (csa/valid? ::hops/hop (sut/get-ingredient :hops (rand-nth (keys (sut/all-ingredients-by-type :hops))))))
    (is (csa/valid? ::yeasts/yeast (sut/get-ingredient :yeasts (rand-nth (keys (sut/all-ingredients-by-type :yeasts))))))))

(deftest get-ingredient-by-name-test
  (testing "Ingredients can be pulled by name"
    (is (= 1 (count (sut/get-ingredient-by-name :fermentables "Belgian Candi Syrup - 45L "))))
    (is (map? (:belgian-candi-syrup-45l (sut/get-ingredient-by-name :fermentables "Belgian Candi Syrup - 45L "))))
    (is (empty? (sut/get-ingredient-by-name :fermentables "A Fake Ingredient")))))
