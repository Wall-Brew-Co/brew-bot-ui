(ns brew-bot-ui.text-pages.static-content
  "Static text displayed on the About Me Page"
  (:require [brew-bot-ui.text-pages.about-me :as am]
            [brew-bot-ui.text-pages.contributors :as contrib]
            [brew-bot-ui.text-pages.home-page :as hp]
            [brew-bot-ui.text-pages.not-found :as nf]
            [brew-bot-ui.text-pages.random-generators :as ran-gen]
            [brew-bot-ui.text-pages.weighted-generators :as wei-gen]))

(def about-me am/about-me)
(def home-page hp/home-page)
(def random-generators ran-gen/random-generators)
(def weighted-generators wei-gen/weighted-generators)
(def contributors contrib/contributors)
(def not-found nf/not-found)
