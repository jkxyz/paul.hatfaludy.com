(defproject pah "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0-alpha4"]
                 [com.stuartsierra/component "0.3.2"]
                 [ring "1.7.0-RC1"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [clj-http "3.9.0"]
                 [cheshire "5.8.0"]]
  :plugins [[cider/cider-nrepl "0.17.0"]]
  :profiles {:uberjar {:aot :all
                       :main pah.main}
             :repl {:source-paths ["dev"]}})
