(ns brew-bot-ui.recipes.views
  (:require [antizer.reagent :as ant]
            [brew-bot.util :as util]
            [cljs.pprint :as pprint]
            [nnichols.palette :as palette]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn pluralize
  "Naively pluralize `string` based on `amount`
   TODO: extract this"
  [amount string]
  (if (< 1 amount)
    (str string "s")
    string))

(defn ingredient-bill
  [title type ingredients]
  [:div {:style {:padding-top "10px"}}
   [:h3 title]
   (into [:ul]
         (for [ingredient (sort-by (comp - :weight) (vals ingredients))
               :let [display-name (:name ingredient)
                     weight       (:weight ingredient)
                     weight-unit  (pluralize weight (if (= type :hops) "ounce" "pound"))]]
           ^{:key ingredient} [:li (str display-name ": " weight " " weight-unit)]))])

(defn recipe-projections
  [recipe]
  (fn [recipe]
    [:ul {:style {:max-width "200px"
                  :border (str "5px solid " (palette/srm-number-to-rgba (:sru-color recipe)))}}
     [:li (str "OG: "  (pprint/cl-format false  "~,3f" (:gravity recipe)))]
     [:li (str "ABV: " (pprint/cl-format false  "~,2f" (:abv recipe)) "%")]
     [:li (str "SRM Color: " (int (:sru-color recipe)))]]))

(defn recipe-display-page
  []
  (fn []
    (let [generated-recipe (rf/subscribe [:generated-recipe])
          generator        (rf/subscribe [:generator-type])
          yeast            (first (vals (:yeasts @generated-recipe)))]
      [:div {:style {:padding "0px 0px 20px 10px"}}
       [:h2 "Your Recipe"]
       [:p "Projections"]
       [recipe-projections @generated-recipe]
       [:div {:style {:padding-left "5px"}}
        [ingredient-bill "Grains" :grains (:grains @generated-recipe)]
        (when-let [extracts (seq (:extracts @generated-recipe))]
          [ingredient-bill "Extracts" :extracts extracts])
        [ingredient-bill "Hops" :hops (:hops @generated-recipe)]
        [:div {:style {:padding-top "10px"}}
         [:h3 "Yeast"]
         [:ul
          [:li (str (:manufacturer yeast) " - " (:product-number yeast) ": " (:name yeast))]]]]
       [:span {:style {:padding-top "10px"}}
        [ant/button {:type "primary"
                     :on-click #(rf/dispatch [:generate-recipe @generator])} "Retry with current settings"]]])))
