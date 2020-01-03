(ns brew-bot-ui.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :current-page
 (fn [db _]
   (:current-page db)))
