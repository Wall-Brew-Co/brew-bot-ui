(ns brew-bot-ui.common-specs-test
  (:require [clojure.spec.alpha :as csa]
            [brew-bot-ui.common-specs :as common]
            #? (:clj  [clojure.test :refer [deftest is testing run-tests]])
            #? (:cljs [cljs.test    :refer-macros [deftest is testing run-tests]])))

(deftest metadata-test
  (testing "Ensure all spec definitions are well-formatted"
    (is (csa/valid? ::common/metadata {}))
    (is (csa/valid? ::common/metadata {:foo "bar"}))
    (is (not (csa/valid? ::common/metadata "{:foo \"bar\"}")))
    (is (not (csa/valid? ::common/metadata [:foo "bar"])))
    (is (not (csa/valid? ::common/metadata nil)))))
