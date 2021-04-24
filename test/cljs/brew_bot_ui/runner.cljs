(ns brew-bot-ui.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [brew-bot-ui.common-specs-test]
            [brew-bot-ui.shared.components.util-test]
            [brew-bot-ui.shared.ingredient-utils-test]))

(doo-tests 'brew-bot-ui.common-specs-test
           'brew-bot-ui.shared.components.util-test
           'brew-bot-ui.shared.ingredient-utils-test)
