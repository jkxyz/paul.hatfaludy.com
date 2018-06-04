(ns pah.core
  (:require [compojure.core :refer [GET] :as compojure]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5]]))

(defn layout [{:keys [title] :or {title "Hatfaludy Paul-Alin"}} content]
  (html5
    {:lang "en"}
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:title title]
    [:div content]))

(defn home-page [req]
  (layout
    {}
    [:h1 "H"]))

(defn routes []
  (compojure/routes
    (GET "/" [] home-page)))
