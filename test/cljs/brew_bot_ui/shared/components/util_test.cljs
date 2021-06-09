
(ns brew-bot-ui.shared.components.util-test
  (:require [cljs.test :refer-macros [is deftest]]
            [brew-bot-ui.shared.components.util :as sut]))

(deftest join-classes
  (is (= "A B D" (sut/join-classes [["A"] "B" [[nil]] "D"])))
  (is (= "A B D" (sut/join-classes [["A"]] "B" [[nil]] "D")))
  (is (= "A B D" (sut/join-classes "A" "B" "D")))
  (is (= "A B D" (sut/join-classes "A B D")))
  (is (= "" (sut/join-classes []))))

(deftest ->html-id-test
  (is (= "hello" (sut/->html-id "hello")))
  (is (= "hello" (sut/->html-id "hello   ")))
  (is (= "hello_there" (sut/->html-id "hello there")))
  (is (= nil (sut/->html-id nil))))
