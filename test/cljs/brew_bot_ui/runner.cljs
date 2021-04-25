(ns brew-bot-ui.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [brew-bot-ui.common-specs-test]
            [brew-bot-ui.shared.components.util-test]
            [brew-bot-ui.recipe-builder.events-test]
            [brew-bot-ui.utils.fermentables-test]
            [brew-bot-ui.utils.ingredients-test]))

(doo-tests 'brew-bot-ui.common-specs-test
           'brew-bot-ui.shared.components.util-test
           'brew-bot-ui.recipe-builder.events-test
           'brew-bot-ui.utils.fermentables-test
           'brew-bot-ui.utils.ingredients-test)
