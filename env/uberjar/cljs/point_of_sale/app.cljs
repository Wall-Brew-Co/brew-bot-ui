(ns brew-bot-ui.app
  (:require [brew-bot-ui.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init)
