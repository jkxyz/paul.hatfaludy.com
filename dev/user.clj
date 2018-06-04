(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [pah.system :refer [new-development-system]]
            [com.stuartsierra.component :as component]))

(def ^:dynamic *system*)

(defn init []
  (alter-var-root #'*system* (constantly (new-development-system))))

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
