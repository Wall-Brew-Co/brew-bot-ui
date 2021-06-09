(ns brew-bot-ui.shared.request
  (:require [ajax.core :as ajax]
            [brew-bot-ui.shared.config :as config]
            [nnichols.util :as util]))

(def remote-url config/remote-url)
(def x-session-id (util/uuid))
(def x-request-id (atom 1))

(defn json-request-response
  ([]
   (json-request-response false))

  ([maintain-request-id?]
   (let [request-id @x-request-id]
     (when-not maintain-request-id? (swap! x-request-id inc))
     {:format          (ajax/json-request-format)
      :response-format (ajax/json-response-format {:keywords? true})
      :headers         {"X-CSRF-Token" js/csrfToken
                        "x-session-id" x-session-id
                        "x-request-id" request-id}})))
