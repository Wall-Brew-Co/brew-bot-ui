(defproject brew-bot-ui "0.1.0"
  :description "brew-bot, but in space!"
  :url "https://github.com/nnichols/brew-bot-ui"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[circleci/rollcage "1.0.203"]
                 [clojure.jdbc/clojure.jdbc-c3p0 "0.3.3"]
                 [compojure "1.6.1"]
                 [honeysql "0.9.8"]
                 [nilenso/honeysql-postgres "0.2.6"]
                 [nnichols "0.5.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ragtime "0.8.0"]
                 [ring-logger "1.0.1"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-defaults "0.3.2"]
                 [trptcolin/versioneer "0.2.0"]]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :target-path "target/"
  :min-lein-version "2.0.0"
  :uberjar-name "brew-bot-ui.jar"
  :main brew-bot-ui.main
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler brew-bot-ui.server/app}
  :aliases {"test-build" ["do" "clean" ["test"]]}
  :profiles {:production {:env {:production true}}
             :repl {:main brew-bot-ui.core}
             :dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                   [ring/ring-mock "0.4.0"]]}})
