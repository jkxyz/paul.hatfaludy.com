(ns pah.core
  (:require [compojure.core :refer [GET] :as compojure]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn layout [{:keys [title] :or {title "Hatfaludy Paul-Alin"}} content]
  (html5
    {:lang "en"}
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href "/assets/stylesheets/application.css"}]
    [:title title]
    [:div.container
     [:header.header
      [:h1.header-title
       [:a {:href "/"} "Hatfaludy Paul-Alin"]]
      [:h2.header-sub
       "Photographer in Oradea."]]
     content]))

(defn home-page-body []
  (layout
    {}
    [:div]))

(defn home-page [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (home-page-body)})

(defn routes []
  (compojure/routes
    (GET "/" req (home-page req))))

(defn app-handler []
  (-> (routes)
      (wrap-defaults site-defaults)))
