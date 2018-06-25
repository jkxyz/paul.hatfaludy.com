(ns pah.main
  (:gen-class)
  (:require [pah.system :refer [new-production-system]]
            [com.stuartsierra.component :as component]))

(defn get-port []
  (or (some-> (System/getenv "PORT") Integer/parseInt)
      3000))

(defn -main [& args]
  (component/start (new-production-system {:flickr-api-key ""
                                           :port (get-port)})))
