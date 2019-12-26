(defproject brew-bot-ui "0.0.0"
  :description "brew-bot, but in space!"
  :url "https://github.com/nnichols/brew-bot-ui"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [nnichols "0.5.0"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-logger "1.0.1"]
                 [trptcolin/versioneer "0.2.0"]]
  :min-lein-version "2.0.0"
  :uberjar-name "brew-bot-ui.jar"
  :main brew-bot-ui.main
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler brew-bot-ui.server/app}
  :aliases {"test-build" ["do" "clean" ["test"]]}
  :profiles {:production {:env {:production true}}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring/ring-mock "0.4.0"]]}})
