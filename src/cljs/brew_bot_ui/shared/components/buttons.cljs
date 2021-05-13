(ns brew-bot-ui.shared.components.buttons
   (:require [brew-bot-ui.shared.components.util :as util]))

(def button-class
  {:class (util/join-classes "text-lg" "cursor-pointer" "focus:outline-none" "focus:ring" "btn-rounded")})

(defn button
  ([children]
   [button {} children])

  ([_attrs _children]
   (fn [attrs children]
     [:button (merge-with merge button-class attrs) children])))

(def button-link-class
  {:class (util/join-classes "text-lg" "cursor-pointer" "focus:outline-none" "focus:ring" "btn-rounded" "hover:bg-emerald" "hover:text-white")})

(defn button-link
  ([children]
   [button-link {} children])

  ([_attrs _children]
   (fn [attrs children]
     [:button (merge-with merge button-link-class attrs) children])))
