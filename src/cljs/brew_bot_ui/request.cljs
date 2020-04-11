(ns brew-bot-ui.request
  (:require [ajax.core :as ajax]))

(def ^:const remote-url
  "https://brew-bot-server.herokuapp.com")

(def ^:const json-request-response
  {:format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})})
