(ns pah.system
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn handler [req]
  {:status 200
   :body "Hello World"})

(defrecord Server [server]
  component/Lifecycle
  (start [this]
    (if-not server
      (assoc this :server (run-jetty handler {:port 3000 :join? false}))
      this))
  (stop [this]
    (if server
      (do
        (.stop server)
        (assoc this :server nil))
      this)))

(defn new-server [] (map->Server {}))

(defn new-development-system []
  (component/system-map 
    :server (new-server)))
