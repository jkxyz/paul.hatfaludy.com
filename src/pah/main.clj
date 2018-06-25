(ns pah.main
  (:gen-class)
  (:require [pah.system :refer [new-production-system]]
            [com.stuartsierra.component :as component]))

(defn -main [& args]
  (component/start (new-production-system {:flickr-api-key ""})))
