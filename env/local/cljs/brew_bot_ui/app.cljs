(ns ^:figwheel-no-load brew-bot-ui.app
  (:require [brew-bot-ui.recipe-builder.main :as bb]))

(enable-console-print!)

(bb/init)
