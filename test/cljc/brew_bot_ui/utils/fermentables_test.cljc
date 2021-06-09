(ns brew-bot-ui.utils.fermentables-test
  (:require [brew-bot-ui.utils.ingredients :as ing]
            [brew-bot-ui.utils.fermentables :as sut]
            #? (:clj  [clojure.test :refer [deftest is testing]])
            #? (:cljs [cljs.test    :refer-macros [deftest is testing]])))

(deftest grain?-test
  (testing "Grains can be detected by their type"
    (is (true? (sut/grain? (:acid-malt (ing/get-ingredient-by-name :fermentables "Acid Malt")))))
    (is (false? (sut/grain? (:belgian-candi-syrup-45l (ing/get-ingredient-by-name :fermentables "Belgian Candi Syrup - 45L ")))))))

(deftest fermentable->color-unit-test
  (testing "The appropriate unti suffix can be determined for a fermentable"
    (is (= "˚L" (sut/fermentable->color-unit (:acid-malt (ing/get-ingredient-by-name :fermentables "Acid Malt")))))
    (is (= "˚SRM" (sut/fermentable->color-unit (:belgian-candi-syrup-45l (ing/get-ingredient-by-name :fermentables "Belgian Candi Syrup - 45L ")))))))
