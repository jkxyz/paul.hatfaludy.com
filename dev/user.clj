(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [pah.system :refer [new-development-system]]
            [com.stuartsierra.component :as component]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn read-dev-config []
  (with-open [r (java.io.PushbackReader. (io/reader "dev-config.edn"))]
    (edn/read r)))

(def ^:dynamic *system*)

(defn init []
  (alter-var-root
    #'*system* 
    (constantly 
      (new-development-system (read-dev-config)))))

(defn start []
  (alter-var-root #'*system* component/start))

(defn stop []
  (alter-var-root #'*system* component/stop))

(defn go []
  (init)
  (start))

(defn reload []
  (stop)
  (refresh :after 'user/go))
