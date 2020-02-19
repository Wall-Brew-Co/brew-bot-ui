(ns brew-bot-ui.common-specs
  "Specs used to negotiate data between client/server"
  (:require [clojure.spec.alpha :as s]
            [nnichols.spec :as nspec]))

; For convenience, so we only have to include one ns
(s/def ::int ::nspec/n-integer)
(s/def ::uuid ::nspec/n-uuid)

(s/def ::metadata map?)
(s/def ::generator
  #{:random :limited-random :weighted-guided :weighted-random})
