(ns pah.pages.core
  (:require [hiccup.page :refer [html5]]))

(defn root-path []
  (or (some->> (System/getenv "UP_STAGE") (str "/" "/"))
      "/"))

(defn layout 
  [{:keys [title] :or {title "Hatfaludy Paul-Alin"}}
   content]
  (html5
    {:lang "en"}
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href (str (root-path) "assets/stylesheets/application.css")}]
    [:title title]
    [:div.container
     [:header.header
      [:h1.header-title [:a {:href (root-path)} "Hatfaludy Paul-Alin"]]
      [:h2.header-sub "Photographer in Oradea."]]
     content]))
