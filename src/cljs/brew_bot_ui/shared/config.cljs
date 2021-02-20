(ns brew-bot-ui.shared.config
  (:require [clojure.string :as cs]))

(def config
  "Client-side configuration map."
  {:wb-prod {:remote-url "https://brewbot.wallbrew.com"
             :features   {:google-analytics true}}
   :hk-prod {:remote-url "https://brew-bot-server.herokuapp.com"
             :features   {:google-analytics true}}
   :dev     {:remote-url "http://localhost:8080"
             :features   {:google-analytics false}}})

(def config-by-domain
  "Map from domains to config key paths."
  (array-map
    "wallbrew"  [:wb-prod]
    "heroku"    [:hk-prod]
    "localhost" [:dev]))

(defn get-domain-by-location
  "Returns the domain corresponding to the current browser location."
  []
  (let [loc (.-href (.-location js/document))
        _ (println loc)]
    (or (first (filter #(cs/includes? loc %) (keys config-by-domain)))
        "localhost")))

(defn get-config
  "Accepts a single key or a collection of keys for accessing nested maps.
   Returns the config value for the current environment."
  [c]
  (get-in config (concat (config-by-domain (get-domain-by-location)) (if (coll? c) c [c]))))

(def deployment-env
  (first (config-by-domain (get-domain-by-location))))

(def google-analytics-on?
  (get-config [:features :google-analytics]))

(def remote-url
  (get-config [:remote-url]))
