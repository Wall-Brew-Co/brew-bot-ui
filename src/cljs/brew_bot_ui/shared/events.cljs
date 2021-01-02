(ns brew-bot-ui.shared.events
  (:require [brew-bot-ui.shared.request :as request]
            [re-frame.core :as rf]))

(defonce repeating-event-timers (atom {}))
(defonce debounce-event-keys (atom nil))

(rf/reg-fx
 :start-repeating-event
 (fn [{:keys [event frequency]}]
   (swap! repeating-event-timers
          (fn [timers]
            (when-let [previous-interval-id (get timers event)]
                   ;stop previous timer if we try to start the same event twice
              (js/clearInterval previous-interval-id))
            (assoc timers event (js/setInterval #(rf/dispatch event) frequency))))))

(rf/reg-fx
 :stop-repeating-event
 (fn [event]
   (js/clearInterval (@repeating-event-timers event))
   (swap! repeating-event-timers #(dissoc % event))))

(defn dispatch-if-not-superceded
  [{:keys [key event time-received]}]
  (when (= time-received (get @debounce-event-keys key))
    (rf/dispatch event)))

(defn dispatch-later
  [{:keys [delay] :as debounce}]
  (js/setTimeout (fn [] (dispatch-if-not-superceded debounce)) delay))

(rf/reg-fx
 :dispatch-debounce
 (fn dispatch-debounce [debounce]
   (let [timestamp (.getTime (js/Date.))]
     (swap! debounce-event-keys assoc (:key debounce) timestamp)
     (dispatch-later (assoc debounce :time-received timestamp)))))

;; Dynos will idle if they're inactive, so we ping them on an interval
(rf/reg-event-fx
 :stay-alive
 (fn [_ _]
   {:start-repeating-event {:frequency 5000
                            :event [:get-version]}}))

(rf/reg-event-fx
 :get-version
 (fn [_ [_]]
   {:http-xhrio [(merge (request/json-request-response)
                        {:method :get
                         :uri (str request/remote-url "/info")
                         :on-success [:get-version-succeeded]
                         :on-failure [:no-op]})]}))

(rf/reg-event-db
 :get-version-succeeded
 (fn [db [_ {:keys [version]}]]
   (assoc db :version version)))

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
      :http-xhrio (merge (request/json-request-response)
                         {:method     :put
                          :uri        (str request/remote-url "/log")
                          :params     (merge data base-info)
                          :on-success [:no-op]
                          :on-failure [:no-op]})})))
