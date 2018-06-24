(ns pah.core
  (:require [compojure.core :refer [GET] :as compojure]
            [compojure.route :refer [not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [pah.pages.home :as pages.home]))

(defn routes [flickr]
  (compojure/routes
    (GET "/" request (pages.home/handle request flickr))
    (not-found "Not Found")))

(defn app-handler [flickr]
  (-> (routes flickr)
      (wrap-defaults site-defaults)))
