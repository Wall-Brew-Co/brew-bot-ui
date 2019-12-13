(ns brew-bot-ui.server-test
  (:require [brew-bot-ui.server :refer :all]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(def ^:private local-url-stem "https://localhost:")
(def ^:private local-port "3000")

(defn local-api-test
  "Helper fn to wrap `mock/request` to direct test requests to use https"
  [request-type route & [opts]]
  (app (mock/request request-type (str local-url-stem local-port route) opts)))

(deftest test-routes
  (testing "Main route"
    (let [response (local-api-test :get "/")]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello from Heroku"))))

  (testing "SSL/HTTPS redirect"
    (is (= 301 (:status (app (mock/request :get "/"))))))

  (testing "page not-found route"
    (let [response (local-api-test :get "/invalid")]
      (is (= (:status response) 404)))))
