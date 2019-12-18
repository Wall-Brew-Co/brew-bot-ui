(ns brew-bot-ui.middleware
  (:require [brew-bot-ui.config :as config]
            [clojure.tools.logging :as log]
            [nnichols.http :as http]
            [ring.logger :as logger]))

(defn wrap-ignore-trailing-slash
  "Modifies the request uri before calling the handler.
   Removes a single trailing slash from the end of the uri if present.
   Related to: https://github.com/weavejester/compojure/wiki/Common-Problems"
  [handler]
  (fn [request]
    (let [uri (:uri request)
          trailing-slash? (and (not= "/" uri)
                               (.endsWith uri "/"))
          effective-uri (if trailing-slash?
                          (subs uri 0 (dec (count uri)))
                          uri)]
      (handler (assoc request :uri effective-uri)))))

(defn wrap-internal-error
  "Takes exceptions thrown by the handler and logs them without barfing them out"
  [handler]
  (fn [request]
    (try (handler request)
         (catch Throwable t
           (log/error {:message "Uncaught handler error"
                       :x-session-id (get-in request [:headers "x-session-id"])
                       :error t})
           (http/bodiless-json-response 500)))))

(defn wrap-logging
  "Silences logging for test environments"
  [handler]
  (fn [request]
    (let [wrapped-handler (-> handler
                              logger/wrap-log-response
                              logger/wrap-log-request-params)]
      (if config/log-routes?
        (wrapped-handler request)
        (handler request)))))
