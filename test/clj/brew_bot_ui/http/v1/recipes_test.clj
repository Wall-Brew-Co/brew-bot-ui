(ns brew-bot-ui.http.v1.recipes-test
  (:require [brew-bot-ui.http.helpers :as help]
            [clojure.test :refer :all]))

(deftest test-routes
  (testing "Get recipes route"
    (let [response (help/local-api-test :get "/v1/recipes/not-a-guid")]
      (is (= (:status response) 404)))))
