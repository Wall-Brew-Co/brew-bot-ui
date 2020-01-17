(ns brew-bot-ui.http.route-specs-test
  (:require [brew-bot-ui.http.route-specs :as rs]
            [clojure.spec.alpha :as csa]
            [clojure.test :refer :all]
            [nnichols.spec :as nspec]
            [nnichols.util :as nu]))

(csa/def ::int-val ::nspec/n-integer)
(csa/def ::uuid-val ::nspec/n-uuid)

(csa/def ::test-spec
  (csa/keys :req-un [::int-val ::uuid-val]))

(deftest conform-route-test
  (testing "Ensure only valid data can be passed through to the route handler"
    (let [conforming-map {:int-val 100 :uuid-val (nu/uuid)}
          bad-map {:int-val "banana"}
          test-route-fn (fn [m] (* 0 (:int-val m)))]
      (is (= 16 (rs/conform-route ::nspec/n-integer 15 inc)))
      (is (= 16 (rs/conform-route ::nspec/n-integer "17" dec)))
      (is (zero? (rs/conform-route ::test-spec conforming-map test-route-fn)))
      (is (= 400 (:status (rs/conform-route ::test-spec bad-map test-route-fn)))))))
