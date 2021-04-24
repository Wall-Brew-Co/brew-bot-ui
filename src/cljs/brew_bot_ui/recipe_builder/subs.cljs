(ns brew-bot-ui.recipe-builder.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :current-page
 (fn [db _]
   (:current-page db)))

(rf/reg-sub
 :recipe
 (fn [db _]
   (:recipe db)))

(rf/reg-sub
 :fermentables
 (fn [db _]
   (get-in db [:recipe :fermentables])))

(rf/reg-sub
 :hops
 (fn [db _]
   (get-in db [:recipe :hops])))

(rf/reg-sub
 :yeasts
 (fn [db _]
   (get-in db [:recipe :yeasts])))

(rf/reg-sub
 :yeast-search-criteria
 (fn [db _]
   (get-in db [:search-boxes :yeast])))

(rf/reg-sub
 :hop-search-criteria
 (fn [db _]
   (get-in db [:search-boxes :hop])))

(rf/reg-sub
 :fermentable-search-criteria
 (fn [db _]
   (get-in db [:search-boxes :fermentable])))
