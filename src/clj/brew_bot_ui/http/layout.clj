(ns brew-bot-ui.http.layout
  (:require [selmer.parser :as parser]
            [selmer.filters :as filters]
            [markdown.core :as md]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clojure.java.io :as io]
            [ring.util.http-response :as ring]
            [ring.util.anti-forgery :as af]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

(declare ^:dynamic *app-context*)
(parser/set-resource-path! (io/resource "public"))
(parser/add-tag! :csrf-field (fn [_ _] (af/anti-forgery-field)))
(filters/add-filter! :markdown (fn [content] [:safe (md/md-to-html-string content)]))

;this is set whenever the app starts to force reload assets
(def fingerprint
  (str (c/to-long (t/now))))

(defn render
  "renders the HTML template located relative to resources/public"
  [template & [params]]
  (ring/content-type
   (ring/ok
    (parser/render-file
     template
     (assoc params
            :fingerprint     fingerprint
            :page            template
            :csrf-token      *anti-forgery-token*
            :servlet-context *app-context*)))
   "text/html; charset=utf-8"))
