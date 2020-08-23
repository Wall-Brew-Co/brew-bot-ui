(ns brew-bot-ui.http.server
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.http.layout :as layout]
            [brew-bot-ui.http.middleware :as middleware]
            [brew-bot-ui.http.v1.recipes :as recipes]
            [brew-bot-ui.logging :as log]
            [compojure.core :refer [defroutes routes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [nnichols.http :as nhttp]))

(defroutes default-routes
  (GET "/" []
    (middleware/wrap-no-cache (layout/render "index.html")))

  (GET "/heartbeat" []
    (nhttp/bodiless-json-response 200))

  (GET "/info" []
    {:status 200
     :body   (config/app-info)})

  (PUT "/log" [_ :as {:keys [body-params]}]
    (let [logging-fn (case (:level body-params)
                       "fatal" log/fatal
                       "error" log/error
                       "warn"  log/warn
                       "info"  log/info
                       log/info)]
      (logging-fn (assoc body-params :version config/app-info))
      {:status 201})))

(def app-routes
  (routes #'default-routes
          #'recipes/routes
          (route/not-found (layout/render "404.html"))))

(def app
  "The actual ring handler that is run."
  (middleware/wrap-base app-routes))
