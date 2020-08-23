(ns brew-bot-ui.dev-middleware
  (:require [ring.middleware.reload :refer [wrap-reload]]
            [selmer.middleware :refer [wrap-error-page]]
            [prone.middleware :refer [wrap-exceptions]]))

(defn wrap-dev [handler]
  (-> handler
      (wrap-reload {:dirs ["src" "checkouts"]})
      wrap-error-page
      wrap-exceptions))
