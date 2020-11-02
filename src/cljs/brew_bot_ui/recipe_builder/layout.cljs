(ns brew-bot-ui.recipe-builder.layout
  (:require [brew-bot-ui.shared.components.util :as cu]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn ingredient-box
  [ingredient-type]
  [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "flex-none")
         :style {:padding "5px" :margin "5px" :min-width "300px"}}
   [:div {:class (cu/join-classes "text-center" "py-2")}
    [:h3 ingredient-type]]
   [:div {:class (cu/join-classes "text-base" "py-2")}
    [:p "This is the body"]]
   [:div {:class (cu/join-classes "text-sm" "py-2")}
    [:p "This is the footer"]]])


(defn main-panel
  []
  (let [current-page (rf/subscribe [:current-page])]
    (fn []
      [:div
       [:h1 (str "Hello there: " @current-page)]
       [:div {:class (cu/join-classes "flex" "flex-wrap" "sm:flex-col" "md:flex-col" "lg:flex-row" "xl:flex-row")}
         [ingredient-box "Fermentables"]
         [ingredient-box "Hops"]]])))
