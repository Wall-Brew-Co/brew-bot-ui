(ns brew-bot-ui.utils.fermentables
  (:require [nnichols.string :as nns]))

(defn grain?
  [fermentable]
  (nns/string-compare (:type fermentable) "grain"))

(defn fermentable->color-unit
  [fermentable]
  (if (grain? fermentable) "˚L" "˚SRM"))
