
(ns brew-bot-ui.shared.components.util-test
  (:require [cljs.test :refer-macros [is deftest]]
            [brew-bot-ui.shared.components.util :as sut]))

(deftest join-classes
  (is (= "A B D" (sut/join-classes [["A"] "B" [[nil]] "D"])))
  (is (= "A B D" (sut/join-classes [["A"]] "B" [[nil]] "D")))
  (is (= "A B D" (sut/join-classes "A" "B" "D")))
  (is (= "A B D" (sut/join-classes "A B D")))
  (is (= "" (sut/join-classes []))))
