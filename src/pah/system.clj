(ns pah.system
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [pah.core :refer [app-handler]]
            [pah.flickr :as flickr]))

(defrecord Server [server flickr-api-info]
  component/Lifecycle
  (start [this]
    (if-not server
      (let [config {:port 3000 :join? false :send-server-version? false}
            handler (app-handler flickr-api-info)]
        (assoc this :server (run-jetty handler config)))
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
    :server (component/using (new-server)
                             {:flickr-api-info :flickr-api-info})
    :flickr-api-info (flickr/api-info "")))
