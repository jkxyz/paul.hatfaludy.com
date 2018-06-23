(ns pah.system
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [pah.core :refer [app-handler]]
            [flickr.core :as flickr]))

(defrecord Server [server flickr]
  component/Lifecycle
  (start [this]
    (if-not server
      (let [config {:port 3000 :join? false :send-server-version? false}
            handler (app-handler flickr)]
        (assoc this :server (run-jetty handler config)))
      this))
  (stop [this]
    (if server
      (do
        (.stop server)
        (assoc this :server nil))
      this)))

(defn new-server [] (map->Server {}))

(defn new-development-system [{:keys [flickr-api-key]}]
  (component/system-map
    :flickr (flickr/api-info flickr-api-key)
    :server (component/using (new-server)
                             {:flickr :flickr})))
