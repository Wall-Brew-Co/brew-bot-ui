(defproject brew-bot-ui "0.5.3"
  :description "brew-bot, but in space!"
  :url "https://github.com/nnichols/brew-bot-ui"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[antizer "0.3.1"]
                 [brew-bot "2.0.0"]
                 [cheshire "5.9.0"]
                 [cider/piggieback "0.4.2"]
                 [circleci/rollcage "1.0.203"]
                 [clj-commons/secretary "1.2.4"]
                 [clj-time "0.15.2"]
                 [cljsjs/react "16.11.0-0"]
                 [cljsjs/react-dom "16.11.0-0"]
                 [com.fzakaria/slf4j-timbre "0.3.17"]
                 [com.zaxxer/HikariCP "3.3.1"]
                 [compojure "1.6.1"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [day8.re-frame/test "0.1.5"]
                 [district0x.re-frame/google-analytics-fx "1.0.0"]
                 [figwheel-sidecar "0.5.19"]
                 [honeysql "0.9.8"]
                 [markdown-clj "1.10.0"]
                 [metosin/ring-http-response "0.9.1"]
                 [nilenso/honeysql-postgres "0.2.6"]
                 [nnichols "0.7.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597" :scope "provided"]
                 [org.postgresql/postgresql "42.1.1"]
                 [ragtime "0.8.0"]
                 [re-frame "0.10.9"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.3"]
                 [ring-logger "1.0.1"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-defaults "0.3.2"]
                 [seancorfield/next.jdbc "1.0.13"]
                 [selmer "1.12.18"]
                 [trptcolin/versioneer "0.2.0"]]

  :plugins [[com.jakemccrary/lein-test-refresh "0.19.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.19"]
            [lein-ring "0.12.5"]]

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj" "test/cljc" "test/cljs"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s"
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "resources/test" "target"]
  :min-lein-version "2.5.3"
  :uberjar-name "brew-bot-ui.jar"
  :main ^:skip-aot brew-bot-ui.main

  :figwheel {:http-server-root "public"
             :nrepl-port 7002
             :css-dirs ["resources/public/css"]
             :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :doo {:build "test"
        :alias {:default [:chrome-headless-no-sandbox]}
        :paths {:karma "./node_modules/karma/bin/karma"}
        :karma {:launchers {:chrome-headless-no-sandbox {:plugin "karma-chrome-launcher"
                                                         :name   "ChromeHeadlessNoSandbox"}}
                :config    {"captureTimeout"             210000
                            "browserDisconnectTolerance" 3
                            "browserDisconnectTimeout"   210000
                            "browserNoActivityTimeout"   210000
                            "customLaunchers"            {"ChromeHeadlessNoSandbox"
                                                          {"base"  "ChromeHeadless"
                                                           "flags" ["--no-sandbox" "--disable-dev-shm-usage"]}}}}}

  :ring {:handler brew-bot-ui.server/app}

  :aliases {"prod-build" ["do" "clean" ["cljsbuild" "once" "prod"]]
            "dev-build"  ["do" "clean" ["cljsbuild" "once" "dev"] "figwheel"]
            "test-build" ["do" "clean" ["cljsbuild" "once" "test"] ["doo" "once"] ["test"]]}

  :cljsbuild {:builds [{:id "prod"
                        :source-paths ["src/cljs" "src/cljc"]
                        :compiler {:main "brew-bot-ui.main"
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :parallel-build true}}

                       {:id "dev"
                        :source-paths ["src/cljs" "src/cljc"]
                        :figwheel {:on-jsload "brew_bot_ui.main/init"}
                        :compiler {:main "brew-bot-ui.main"
                                   :asset-path "js/compiled/out"
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map true
                                   :optimizations :none
                                   :parallel-build true
                                   :pretty-print true}}

                       {:id "test"
                        :source-paths ["src/cljs" "src/cljc" "test/cljs" "test/cljc"]
                        :figwheel {:on-jsload "brew_bot_ui.main/init"}
                        :compiler {:main "brew-bot-ui.runner"
                                   :output-to "resources/test/app.js"
                                   :output-dir "resources/test/js/compiled/out"
                                   :optimizations :none
                                   :parallel-build true}}]}

  :profiles {:production {:env {:production true}}
             :uberjar {:omit-source  true
                       :prep-tasks   ["compile" ["cljsbuild" "once" "prod"]]
                       :dependencies [[day8.re-frame/tracing-stubs "0.5.3"]]
                       :aot          :all
                       :source-paths ["env/uberjar/clj"]}
             :repl {:main brew-bot-ui.core}
             :dev {:dependencies [[circleci/bond "0.3.0"]
                                  [doo "0.1.11"]
                                  [javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.4.0"]]
                   :plugins      [[lein-doo "0.1.10"]]}})
