(ns brew-bot-ui.core
  (:require [brew-bot-ui.db.queries :as db]))

(def get-recipe db/get-recipe-by-id)
