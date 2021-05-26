(ns brew-bot-ui.recipe-builder.layout
  (:require [brew-bot-ui.shared.components.buttons :as buttons]
            [brew-bot-ui.shared.components.icons :as icons]
            [brew-bot-ui.shared.components.inputs :as inputs]
            [brew-bot-ui.shared.components.util :as cu]
            [brew-bot-ui.utils.fermentables :as f-utils]
            [clojure.string :as cs]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn recipe-facts
  []
  (let [recipe-name           (r/atom nil)
        recipe-author         (r/atom nil)
        fermentation-volume   (r/atom 0)
        boil-volume           (r/atom 0)
        boil-time             (r/atom 0)
        extraction-efficiency (r/atom 70)]
    (fn []
      [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "sm:w-full" "md:w-full" "lg:w-2/3" "xl:w-2/3")
             :style {:padding   "5px"
                     :margin    "auto"
                     :min-width "300px"}
             :id    "RecipeFactsBox"}
       [:div {:class (cu/join-classes "text-center" "py-2")
              :id    "RecipeFactsHeader"}
        [:h3 "Recipe Builder"]]
       [:div {:id    "RecipeIdentifiers"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-2" "py-2" "justify-around" "text-base")}
        [inputs/text {:style     {:min-width "140px"}
                      :value     @recipe-name
                      :id        "RecipeNameInputBox"
                      :label     "Recipe Name"
                      :on-change #(reset! recipe-name (-> % .-target .-value))}]
        [inputs/text {:style        {:min-width "140px"}
                      :value        @recipe-author
                      :id           "RecipeAuthorInputBox"
                      :label        "Recipe Author"
                      :autoComplete "name"
                      :on-change    #(reset! recipe-author (-> % .-target .-value))}]]
       [:div {:id    "RecipeScaling"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-4" "justify-around" "text-base")}
        [inputs/checkbox {:id    "UnitsCheckbox"
                          :label "Use Imperial Units?"}]
        [inputs/select-box {:style {:min-width "140px"}
                            :id    "BrewingMethodSelectBox"
                            :label "Brewing Method"}
         [{:value "a"
           :name  "a"}
          {:value "b"
           :name  "b"}
          {:value "c"
           :name  "c"}
          {:value "d"
           :name  "d"}
          {:value "e"
           :name  "e"}]]
        [inputs/number {:value     @fermentation-volume
                        :id        "FermentationVolumeInput"
                        :max       10000
                        :min       0
                        :label     "Fermentation Volume"
                        :on-change #(reset! fermentation-volume (-> % .-target .-value))}]
        [inputs/number {:value     @boil-volume
                        :id        "BoilVolumeInput"
                        :max       10000
                        :min       0
                        :label     "Boil Volume"
                        :on-change #(reset! boil-volume (-> % .-target .-value))}]
        [inputs/number {:value     @boil-time
                        :id        "BoilTimeInput"
                        :max       1000
                        :min       0
                        :label     "Boil Time"
                        :on-change #(reset! boil-volume (-> % .-target .-value))}]
        [inputs/number {:value     @extraction-efficiency
                        :id        "ExtractionEfficiencyInput"
                        :max       1000
                        :min       0
                        :label     "Extraction Efficiency"
                        :on-change #(reset! extraction-efficiency (-> % .-target .-value))}]
        [inputs/select-box {:style {:min-width "140px"}
                            :id    "TargetStyleSelectBox"
                            :label "Target Style"}
         [{:value "a"
           :name  "a"}
          {:value "b"
           :name  "b"}
          {:value "c"
           :name  "c"}
          {:value "d"
           :name  "d"}
          {:value "e"
           :name  "e"}]]]
       [:div {:id    "RecipeStatistics"
              :class (cu/join-classes "flex" "flex-wrap" "flex-row" "px-2" "justify-around" "text-base" "divide-x" "divide-gray-400")}
        [:p "IBU"]
        [:p "OG"]
        [:p "FG"]
        [:p "ABV"]
        [:p "Color"]
        [:p "Projected Style"]]])))

(defn ingredient-box
  [_ingredient-type]
  (let [amount-val (r/atom 0)
        search-val (r/atom nil)]
    (fn [ingredient-type]
      [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "px-2" "sm:w-full" "md:w-full" "lg:w-5/12" "xl:w-5/12" "sm:my-4" "md:my-2" "lg:m-2" "xl:m-4")
             :style {:padding "5px" :min-width "300px"}}
       [:div {:class (cu/join-classes "text-center" "py-2")}
        [:h3 ingredient-type]]
       [:div {:class (cu/join-classes "text-base" "py-2")}
        [inputs/text {:value       @search-val
                      :id          "Sample Text Input"
                      :label       "Text"
                      :placeholder "This is a basic text input"
                      :on-change   #(reset! search-val (-> % .-target .-value))}]]
       [:div {:class (cu/join-classes "text-base" "py-2")}
        [inputs/number {:value       @amount-val
                        :id          "Sample number input"
                        :label       "Amount"
                        :min 0
                        :max 10000
                        :on-change   #(reset! amount-val (-> % .-target .-value))}]]
       [:div {:class (cu/join-classes "text-sm" "py-2")}
        [buttons/button (str "Add " ingredient-type)]]])))

(defn empty-search-result
  [_ingredient-type]
  (fn [ingredient-type]
    [:div {:id    (str (name ingredient-type) "EmptySearchResult")
           :class (cu/join-classes "bg-gray-200" "divide-y" "divide-gray-400")
           :style {:width "100%"}}
     [:div {:class (cu/join-classes "py-2" "hover:bg-gray-400")
            :style {:width "99%"}}
      [:h3 {:class (cu/join-classes "text-lg" "font-semibold")}
       "No Results Found"]
      [:span {:class (cu/join-classes "text-sm")}
       [:p (str "Could not find any matching " (name ingredient-type) ".")
        [:br]
        "Please try a different query."]]]]))

(defn ingredient-fact
  [_fact-title _fact-display-value]
  (fn [fact-title fact-display-value]
    [:p [:b [:u fact-title]] (str ": " fact-display-value)]))

(defn fermentable-search-result
  [_fermentable]
  (fn [fermentable]
    (let [id          (cu/->html-id (str (:name fermentable) "SearchResult"))
          color-unit  (f-utils/fermentable->color-unit fermentable)
          max-percent (* 100.0 (:max-in-batch fermentable))]
      [:div {:class    (cu/join-classes "py-2" "hover:bg-gray-400" "cursor-pointer")
             :style    {:width "99%"}
             :id       id
             :on-click #(rf/dispatch [:add-ingredient :fermentables fermentable])}
       [:h3 {:class (cu/join-classes "text-lg" "font-semibold")}
        (:name fermentable)]
       [:span {:class (cu/join-classes "text-md")}
        [ingredient-fact "Potential Gravity" (:potential fermentable)]
        [ingredient-fact "Color" (str (:color fermentable) color-unit)]
        [ingredient-fact "Maximum Recommended Amount" (str max-percent "%")]
        [ingredient-fact "Notes" (:notes fermentable)]]])))

(defn fermentable-addition-box
  [_fermentable]
  (fn [fermentable]
    (let [id          (cu/->html-id (str (:name fermentable) "AdditionBox"))
          color-unit  (f-utils/fermentable->color-unit fermentable)
          max-percent (* 100.0 (:max-in-batch fermentable))]
      [:div {:class (cu/join-classes "py-2")
             :style {:width "99%"}
             :id    id}
       [:div {:class (cu/join-classes "flex" "flex-row")}
        [:h3 {:class (cu/join-classes "text-lg" "font-semibold")}
         (:name fermentable)]
        [icons/icon :delete {}]]
       [:span {:class (cu/join-classes "text-md")}
        [ingredient-fact "Potential Gravity" (:potential fermentable)]
        [ingredient-fact "Color" (str (:color fermentable) color-unit)]
        [ingredient-fact "Maximum Recommended Amount" (str max-percent "%")]
        [ingredient-fact "Notes" (:notes fermentable)]]])))

(defn fermentables-box
  []
  (let [selected-fermentables (rf/subscribe [:fermentables])
        search-criteria       (rf/subscribe [:fermentable-search-criteria])
        search-results        (rf/subscribe [:fermentable-search-results])]
    (fn []
      (let [empty-search? (cs/blank? (get-in @search-criteria [:name]))
            show-search-results?  (and (seq @search-results) (not empty-search?))
            empty-search-results? (and (empty? @search-results) (not empty-search?))]
        [:div {:class (cu/join-classes "rounded-lg" "bg-gray-100" "divide-y" "divide-gray-400" "px-2" "sm:w-full" "md:w-full" "lg:w-5/12" "xl:w-5/12" "sm:my-4" "md:my-2" "lg:m-2" "xl:m-4")
               :style {:padding   "5px"
                       :min-width "300px"}}
         [:div {:class (cu/join-classes "text-center" "py-4")}
          [:h3 {:class (cu/join-classes "text-lg" "font-semibold")}
           "Fermentables"]]
         [:div {:class (cu/join-classes "text-base" "py-2")}
          [inputs/text {:value     (:name @search-criteria)
                        :id        "FermentableSearchInput"
                        :label     "Search by ingredient name"
                        :on-change #(rf/dispatch [:ingredient-search :fermentables :name (-> % .-target .-value)])}]
          (cond
            empty-search-results? [empty-search-result :fermentables]

            show-search-results? [:div {:class (cu/join-classes "bg-gray-200" "divide-y" "divide-gray-400")
                                        :style {:height     300
                                                :width      "100%"
                                                :overflow-y "scroll"
                                                :overflow-x "hidden"}}
                                  (for [fermentable (sort-by :name @search-results)]
                                    ^{:key (:name fermentable)} [fermentable-search-result fermentable])])
          (when-not empty-search?
            [:a {:class (cu/join-classes "text-sm" "text-red-500" "text-underline" "cursor-pointer")
                 :on-click #(rf/dispatch [:ingredient-search :fermentables :name ""])}
             "Clear"])]
         (if (empty? @selected-fermentables)
           [:div {:class (cu/join-classes "text-base" "py-4")} "No Selections Made"]
           [:div {:class (cu/join-classes "text-base" "divide-y" "divide-gray-400")
                  :style {:width "100%"}}
            (for [fermentable (sort-by :name @selected-fermentables)]
              ^{:key (random-uuid)} [fermentable-addition-box fermentable])])]))))

(defn main-panel
  []
  (fn []
    [:div {:id    "BackgroundPanel"
           :class (cu/join-classes "xl:justify-center" "lg:justify-center" "md:justify-center")
           :style {:width "100%"}}
     [recipe-facts]
     [:div {:class (cu/join-classes "flex" "flex-wrap" "sm:flex-col" "md:flex-col" "lg:flex-row" "xl:flex-row" "justify-center")}
      [fermentables-box]
      [ingredient-box "Hops"]
      [ingredient-box "Yeasts"]
      [ingredient-box "Miscellaneous Ingredients"]]]))
