(ns brew-bot-ui.recipe-builder.db)

(def ^:const default-db
  {:current-page :home
   :recipe       {:fermentables {}
                  :hops         {}
                  :yeasts       {}}
   :search-boxes {:fermentables {}
                  :hops         {}
                  :yeasts       {}}
   :search-results {:fermentables []
                    :hops         []
                    :yeasts       []}})
