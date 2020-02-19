(ns brew-bot-ui.http.helpers
  (:require [brew-bot-ui.http.server :refer :all]
            [ring.mock.request :as mock]))

(def ^:private local-url-stem "https://localhost:")
(def ^:private local-port "3000")

(defn local-api-test
  "Helper fn to wrap `mock/request` to direct test requests to use https"
  [request-type route & [opts]]
  (app (mock/request request-type (str local-url-stem local-port route) opts)))
