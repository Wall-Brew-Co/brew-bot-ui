(ns brew-bot-ui.http.html
  (:require [brew-bot-ui.config :as config]
            [selmer.parser :as parser]
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

(defn add-default-page-values
  "Adds default parameters to HTML template files"
  [params]
  (merge
   {:fingerprint         fingerprint
    :google-analytics-id config/google-analytics-id
    :csrf-token          *anti-forgery-token*
    :servlet-context     *app-context*}
   params))

(defn render
  "Renders the HTML template located relative to resources/public with a 200 status code."
  [template & [params]]
  (let [render-params (assoc params :page template)
        render-opts   (add-default-page-values render-params)]
    (ring/content-type (ring/ok (parser/render-file template render-opts)) "text/html; charset=utf-8")))

(defn index [] (render "index.html"))

#_(defn example [] (render "example.html" {:title "Wall Brew"}))

(defn error-page
  "error-details should be a map containing the following keys:
   :status - The HTTP status code.
   :title - A title for the error page
   :message - An error message to display to the user.
   :error-page - A .html file in resources/public to display. Defaults to error.html

   Returns a response map with the error page as the body and the status specified by the status key"
  [{:keys [error-page title message] :as error-details}]
  (let [title       (or title "Internal Server Error")
        message     (or message "Something went wrong. Please try again later.")
        error-page  (or error-page "error.html")
        render-opts (-> error-details
                        (dissoc :error-page)
                        (assoc :title title :message message)
                        add-default-page-values)]
    {:status  (:status error-details)
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body    (parser/render-file error-page render-opts)}))

(defn not-found
  []
  (error-page {:error-page "404.html" :status 404}))

(defn not-authorized
  []
  (error-page {:status 403 :title "Not Authorized" :message "Invalid credentials detected."}))
