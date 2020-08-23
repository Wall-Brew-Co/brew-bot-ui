(ns brew-bot-ui.figwheel
  (:require [brew-bot-ui.http.server :as server]
            [brew-bot-ui.main :as bb]))

(defn init
  [_cfg]
  (bb/-main))

(def handler server/app)
