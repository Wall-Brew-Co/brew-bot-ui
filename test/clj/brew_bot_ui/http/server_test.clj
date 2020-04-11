(ns brew-bot-ui.http.server-test
  (:require [brew-bot-ui.http.helpers :as help]
            [brew-bot-ui.http.server :refer :all]
            [clojure.string :as cs]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(deftest test-routes
  (testing "Main route"
    (let [response (help/local-api-test :get "/")]
      (is (= (:status response) 200))))

  (testing "App info route"
    (let [response (help/local-api-test :get "/info")]
      (is (= (:status response) 200))
      (is (= (get-in response [:body :app]) "brew-bot-ui"))
      (is (and (string? (get-in response [:body :version]))
               (not (cs/blank? (get-in response [:body :version])))))))

  (testing "Heartbeat"
    (let [response (help/local-api-test :get "/heartbeat")]
      (is (= (:status response) 200))))

  (testing "SSL/HTTPS redirect"
    (is (= 301 (:status (app (mock/request :get "/"))))))

  (testing "page not-found route"
    (let [response (help/local-api-test :get "/invalid")]
      (is (= (:status response) 404)))))
