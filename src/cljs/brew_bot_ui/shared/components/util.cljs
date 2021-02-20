(ns brew-bot-ui.shared.components.util
  (:require [clojure.string :as cs]))

(defn join-classes
  "Join several classnames as they'd be referred in CSS"
  [& classes]
  (cs/join " " (remove nil? (flatten classes))))

(defn ->html-id
  "Collapse a string into an HTML id"
  [s]
  (some-> s cs/trim (cs/replace #" " "_")))
