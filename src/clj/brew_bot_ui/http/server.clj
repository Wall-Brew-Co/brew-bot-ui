(ns brew-bot-ui.http.server
  (:require [brew-bot-ui.config :as config]
            [brew-bot-ui.http.middleware :as middleware]
            [compojure.core :refer [defroutes routes GET PUT POST DELETE ANY]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [nnichols.http :as nhttp]))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello from brew-bot!"})

(defroutes default-routes
  (GET "/" []
    (splash))

  (GET "/heartbeat" []
    (nhttp/bodiless-json-response 200))

  (GET "/info" []
    {:status 200 :body (config/app-info)})

  (PUT "/log" [_ :as {:keys [body-params]}]
    ((case (:level body-params)
        "fatal" #(log/fatal %)
        "error" #(log/error %)
        "warn"  #(log/warn %)
        "info"  #(log/info %)
        "debug" #(log/debug %)
        "trace" #(log/trace %)
        #(log/info %)) (-> body-params
                           (dissoc :level)
                           (assoc :version config/app-info)))
    {:status 201}))

(def app-routes
  (routes #'default-routes
          (route/not-found (slurp (io/resource "public/404.html")))))

(def app
  "The actual ring handler that is run."
  (middleware/wrap-base app-routes))
