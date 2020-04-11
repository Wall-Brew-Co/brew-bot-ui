(ns brew-bot-ui.events
  (:require [brew-bot-ui.db :as db]
            [brew-bot-ui.request :as request]
            [nnichols.util :as util]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize-db
 (fn [db [_]]
   (assoc db/default-db
          :x-session-id (util/uuid)
          :x-request-id 1)))

(rf/reg-event-fx
 :reset-db
 (fn [{db :db} [_ page]]
   {:db (merge-with merge db db/default-db)
    :dispatch [:update-current-page page]}))

(rf/reg-event-db
 :update-current-page
 (fn [db [_ page]]
   (assoc db :current-page page)))

(rf/reg-event-db
 :no-op
 (fn [db _] db))

(rf/reg-event-fx
 :send-log
 (fn [{db :db} [_ data]]
   (let [base-info {:at-time      (.toISOString (js/Date.))
                    :x-session-id (:x-session-id db)
                    :x-request-id (:x-request-id db)}]
     {:db (update db :x-request-id inc)
      :http-xhrio (merge request/json-request-response
                         {:method     :put
                          :uri        (str request/remote-url "/log")
                          :params     (merge data base-info)
                          :on-success [:no-op]
                          :on-failure [:no-op]})})))

