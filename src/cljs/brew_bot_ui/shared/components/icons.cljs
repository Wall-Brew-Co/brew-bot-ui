(ns brew-bot-ui.shared.components.icons)

(def icon-svg-paths
  {:add                  "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"
   :beer                 "M19,9h-1.56C17.79,8.41,18,7.73,18,7c0-2.21-1.79-4-4-4c-0.34,0-0.66,0.05-0.98,0.13C12.2,2.45,11.16,2.02,10,2.02 c-1.89,0-3.51,1.11-4.27,2.71C4.15,5.26,3,6.74,3,8.5c0,1.86,1.28,3.41,3,3.86L6,21h11v-2h2c1.1,0,2-0.9,2-2v-6C21,9.9,20.1,9,19,9z M7,10.5c-1.1,0-2-0.9-2-2c0-0.85,0.55-1.6,1.37-1.88l0.8-0.27l0.36-0.76C8,4.62,8.94,4.02,10,4.02c0.79,0,1.39,0.35,1.74,0.65 l0.78,0.65c0,0,0.64-0.32,1.47-0.32c1.1,0,2,0.9,2,2c0,0-3,0-3,0C9.67,7,9.15,10.5,7,10.5z M19,17h-2v-6h2V17z"
   :bug                  "M20 8h-2.81c-.45-.78-1.07-1.45-1.82-1.96L17 4.41 15.59 3l-2.17 2.17C12.96 5.06 12.49 5 12 5c-.49 0-.96.06-1.41.17L8.41 3 7 4.41l1.62 1.63C7.88 6.55 7.26 7.22 6.81 8H4v2h2.09c-.05.33-.09.66-.09 1v1H4v2h2v1c0 .34.04.67.09 1H4v2h2.81c1.04 1.79 2.97 3 5.19 3s4.15-1.21 5.19-3H20v-2h-2.09c.05-.33.09-.66.09-1v-1h2v-2h-2v-1c0-.34-.04-.67-.09-1H20V8zm-6 8h-4v-2h4v2zm0-4h-4v-2h4v2z"
   :chevron-left         "M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
   :chevron-right        "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"
   :close                "M19,6.41L17.59,5L12,10.59L6.41,5L5,6.41L10.59,12L5,17.59L6.41,19L12,13.41L17.59,19L19,17.59L13.41,12L19,6.41Z"
   :filter-list          "M10 18h4v-2h-4v2zM3 6v2h18V6H3zm3 7h12v-2H6v2z"
   :info                 "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"
   :keyboard-arrow-down  "M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41z"
   :keyboard-arrow-left  "M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z"
   :keyboard-arrow-right "M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z"
   :keyboard-arrow-up    "M7.41 15.41L12 10.83l4.59 4.58L18 14l-6-6-6 6z"
   :open-in-new          "M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z"
   :remove               "M 5 11 L 5 13 L 19 13 L 19 11 L 5 11 Z"
   :search               "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"
   :share                "M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92 1.61 0 2.92-1.31 2.92-2.92s-1.31-2.92-2.92-2.92z"})

(def icon-defaults
  {:display        "inline-block"
   :color          "inherit"
   :fill           "currentcolor"
   :vertical-align "inherit"
   :height         "25px"
   :width          "25px"
   :user-select    "none"
   :transition     "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms"})

(defn icon
  "Generates icon given an icon id."
  [_icon-name _attrs]
  (fn [icon-name {:keys [width height style title] :as attrs}]
    (let [icon-path  (get icon-svg-paths icon-name)
          width      (or width 24)
          height     (or height 24)
          view-box   (str "0 0 " height " " width)
          icon-style (merge icon-defaults {:height height
                                           :width  width} style)]
      [:svg (merge attrs
                   {:view-box view-box
                    :style    icon-style})
       (when title
         [:title title])
       [:path {:d icon-path}]])))
