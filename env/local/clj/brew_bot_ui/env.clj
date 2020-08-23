(ns brew-bot-ui.env
  (:require [selmer.parser :as parser]
            [brew-bot-ui.dev-middleware :refer [wrap-dev]]))

(parser/cache-off!)

(def base-middleware wrap-dev)
