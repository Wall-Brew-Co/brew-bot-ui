(ns brew-bot-ui.recipe-builder.main
  "Application launcher for the Recipe Builder"
  (:require [brew-bot-ui.shared.config :as config]
            [brew-bot-ui.recipe-builder.layout :as layout]
            [day8.re-frame.http-fx]
            [district0x.re-frame.google-analytics-fx]
            [reagent.core :as r]
            [re-frame.core :as rf]

            ;; All Event/Sub namespaces must be included to load properly
            [brew-bot-ui.shared.debug]
            [brew-bot-ui.shared.events]
            [brew-bot-ui.recipe-builder.events]
            [brew-bot-ui.recipe-builder.subs]))

(enable-console-print!)

(defn mount-root
  "Render the main app panel, and mount it to the window"
  []
  (rf/clear-subscription-cache!)
  (r/render [#'layout/main-panel] (.getElementById js/document "app")))

(defn ^:export init
  "Initialize the app db, and mount the application.
  Exported to preserve fn name through advanced compilation name munging"
  []
  (district0x.re-frame.google-analytics-fx/set-enabled! config/google-analytics-on?)
  (rf/dispatch [:initialize-db])
  (mount-root))
