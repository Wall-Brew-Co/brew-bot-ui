(ns brew-bot-ui.recipe-builder.events
  (:require [brew-bot-ui.recipe-builder.db :as db]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :initialize-db
 (fn [_ [_]]
   {:dispatch [:stay-alive]
    :db       db/default-db}))
