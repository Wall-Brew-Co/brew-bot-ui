(ns brew-bot-ui.shared.components.inputs
  (:require [brew-bot-ui.shared.components.util :as util]))

(def text-input-class
  {:class (util/join-classes "w-full" "p-4" "rounded-sm" "bg-pearl-gray" "border" "shadow-sm" "has-float-label" "flex" "leading-relaxed" "focus:outline-none" "focus:shadow-outline" "block")})

(def text-style
  {:style {:padding      "0px"
           :overflow     "hidden"
           :resize       "none"
           :white-space  "pre-wrap"}})

(defn label
  "A label to attach to text inputs used in forms.
   Requires the id of the input field it is associated to."
  [id {:keys [label description hidden]}]
  (let [label-class (util/join-classes "text-slate" "tracking-wide" "font-sans" (when hidden "hidden"))
        p-class (util/join-classes "pt-2" "font-sans" "text-sm")]
    [:label {:for id :class label-class}
     label
     (when (not-empty description)
       [:p {:class p-class}
        description])]))

(defn text
  "A standard text input box"
  [_attrs]
  (fn [attrs]
    (let [input-id    (:id attrs)
          label-attrs (select-keys attrs [:label :description])]
      [:div {:style {:margin "0 2 0 2"}}
       [label input-id label-attrs]
       [:input (merge-with merge text-style text-input-class attrs)]])))

(defn select-box
  "A drop-down select box.
   Expects `_options` to be passed as an ordered collection of maps containing a :value and a :name"
  [_attrs _options]
  (fn [attrs options]
    (let [input-id    (:id attrs)
          label-attrs (select-keys attrs [:label :description])]
      [:div {:style {:margin "0 2 0 2"}}
       [label input-id label-attrs]
       [:select (merge-with merge text-style text-input-class attrs)
        (for [option options]
          [:option {:key (:value option)
                    :value (:value option)} (:name option)])]])))

(defn number
  "A number select box.
   Expects :max and :min to be passed as attrs"
  [_attrs]
  (fn [attrs]
    (let [input-id    (:id attrs)
          label-attrs (select-keys attrs [:label :description])]
      [:div {:style {:margin "0 2 0 2"}}
       [label input-id label-attrs]
       [:input (merge-with merge text-style text-input-class {:type "number"} attrs)]])))

(def checkbox-class
  {:class (util/join-classes "checkbox" "absolute" "block" "w-6" "h-6" "rounded-full" "bg-white" "border-4" "appearance-none" "cursor-pointer")})

(defn checkbox
  "A sliding checkbox"
  [_attrs]
  (fn [attrs]
    (let [input-id    (:id attrs)
          label-attrs (select-keys attrs [:label :description])]
      [:div {:style {:margin "0 2 0 2"}}
       [:input (merge-with merge checkbox-class {:type "checkbox"} attrs)]
       [label input-id label-attrs]])))
