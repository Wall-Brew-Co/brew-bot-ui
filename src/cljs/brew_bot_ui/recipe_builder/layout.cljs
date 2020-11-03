(ns brew-bot-ui.recipe-builder.layout
  (:require [brew-bot-ui.shared.components.util :as cu]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn recipe-facts
  []
  [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "sm:w-full" "md:w-full" "lg:w-2/3" "xl:w-2/3")
         :style {:padding "5px" :margin "auto" :min-width "300px"}}
   [:div {:class (cu/join-classes "text-center" "py-2")}
    [:h4 "Recipe Name"]]])

(defn ingredient-box
  [ingredient-type]
  [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "px-2" "sm:w-full" "md:w-full" "lg:w-5/12" "xl:w-5/12" "sm:my-2" "md:my-2" "lg:m-2" "xl:m-2")
         :style {:padding "5px" :min-width "300px"}}
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
      [:div {:class (cu/join-classes "justify-center")
             :style {:width "95%"}}
       [:div [recipe-facts]]
       [:div {:class (cu/join-classes "flex" "flex-wrap" "sm:flex-col" "md:flex-col" "lg:flex-row" "xl:flex-row" "justify-center")}
         [ingredient-box "Fermentables"]
         [ingredient-box "Hops"]
         [ingredient-box "Yeasts"]
         [ingredient-box "Miscellaneous Ingredients"]]])))
