(ns brew-bot-ui.shared.request
  (:require [ajax.core :as ajax]
            [brew-bot-ui.shared.config :as config]))

(def remote-url config/remote-url)

(def ^:const json-request-response
  {:format          (ajax/json-request-format)
   :response-format (ajax/json-response-format {:keywords? true})
   :headers         {"X-CSRF-Token" js/csrfToken}})
