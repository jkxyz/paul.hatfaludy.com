(ns pah.system
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [pah.core :refer [app-handler]]
            [flickr.core :as flickr]))

(def base-server-config {:port 3000
                         :join? false
                         :send-server-version? false})

(defrecord Server [server flickr config]
  component/Lifecycle
  (start [this]
    (if-not server
      (let [config (merge base-server-config config)
            handler (app-handler flickr)]
        (assoc this :server
               (run-jetty handler config)))
      this))
  (stop [this]
    (if server
      (do
        (.stop server)
        (assoc this :server nil))
      this)))

(defn new-server [config] (map->Server {:config config}))

(defn new-development-system [{:keys [flickr-api-key]}]
  (component/system-map
    :flickr (flickr/api-info flickr-api-key)
    :server (component/using (new-server {})
                             {:flickr :flickr})))

(defn new-production-system [{:keys [flickr-api-key]}]
  (component/system-map
    :flickr (flickr/api-info flickr-api-key)
    :server (component/using (new-server {:join? true})
                             {:flickr :flickr})))
