(ns pah.pages.core
  (:require [hiccup.page :refer [html5]]))

(defn layout 
  [{:keys [title] :or {title "Hatfaludy Paul-Alin"}}
   content]
  (html5
    {:lang "en"}
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href "/assets/stylesheets/application.css"}]
    [:title title]
    [:div.container
     [:header.header
      [:h1.header-title [:a {:href "/"} "Hatfaludy Paul-Alin"]]
      [:h2.header-sub "Photographer in Oradea."]]
     content]))
