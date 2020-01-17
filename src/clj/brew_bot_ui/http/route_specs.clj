(ns brew-bot-ui.http.route-specs
  (:require [brew-bot-ui.config :as config]
            [clojure.spec.alpha :as s]
            [nnichols.http :as http]
            [nnichols.spec :as nspec]))

(defn conform-route
  "If `route-values` conforms to the `route-spec`, execute `route-function` against the conformed values.
   If any value does not conform, return a 400 response.
   If configured, the body of that response will explain why the value does not conform."
  [route-spec route-values route-function]
  (let [conformed-values (s/conform route-spec route-values)]
    (if (= ::s/invalid conformed-values)
      (if config/debug-routes?
        {:status 400
         :body (s/explain route-spec route-values)}
        (http/bodiless-json-response 400))
      (route-function conformed-values))))

; For convenience, so we only have to include one ns
(s/def ::int ::nspec/n-integer)
(s/def ::uuid ::nspec/n-uuid)
