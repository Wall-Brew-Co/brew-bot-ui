(ns brew-bot-ui.http.v1.recipes-test
  (:require [brew-bot-ui.http.helpers :as help]
            [clojure.test :as t]))

(t/deftest test-routes
  (t/testing "Get recipes route"
    (let [response (help/local-api-test :get "/v1/recipes/not-a-guid")]
      (t/is (= (:status response) 404)))))
