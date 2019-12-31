(ns brew-bot-ui.server-test
  (:require [brew-bot-ui.http.server :refer :all]
            [clojure.string :as cs]
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
      (is (= (:body response) "Hello from brew-bot!"))))

  (testing "App info route"
    (let [response (local-api-test :get "/info")]
      (is (= (:status response) 200))
      (is (= (get-in response [:body :app]) "brew-bot-ui"))
      (is (and (string? (get-in response [:body :version]))
               (not (cs/blank? (get-in response [:body :version])))))))

  (testing "Heartbeat"
    (let [response (local-api-test :get "/heartbeat")]
      (is (= (:status response) 200))))

  (testing "SSL/HTTPS redirect"
    (is (= 301 (:status (app (mock/request :get "/"))))))

  (testing "page not-found route"
    (let [response (local-api-test :get "/invalid")]
      (is (= (:status response) 404)))))
