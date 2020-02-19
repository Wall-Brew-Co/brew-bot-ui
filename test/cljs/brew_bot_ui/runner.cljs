(ns brew-bot-ui.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [brew-bot-ui.common-specs-test]))

(doo-tests 'brew-bot-ui.common-specs-test)
