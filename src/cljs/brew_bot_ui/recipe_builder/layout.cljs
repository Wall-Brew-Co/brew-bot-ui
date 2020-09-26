(ns brew-bot-ui.recipe-builder.layout
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))


(defn main-panel
  []
  (let [current-page (rf/subscribe [:current-page])]
    (fn []
      [:h1 (str "Hello there: " @current-page)])))
