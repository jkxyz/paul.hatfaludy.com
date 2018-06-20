(ns pah.core
  (:require [compojure.core :refer [GET] :as compojure]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [pah.flickr :as flickr]))

(defn layout [{:keys [title] :or {title "Hatfaludy Paul-Alin"}} content]
  (html5
    {:lang "en"}
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:link {:rel "stylesheet" :href "/assets/styles.css"}]
    [:title title]
    content))

(defn photos [flickr-api-info]
  (let [user (flickr/user flickr-api-info "jkseeker")
        photosets (flickr/user-photosets flickr-api-info user)
        photos (flickr/photoset-photos flickr-api-info (first photosets))]
    photos))

(defn medium-url [flickr-api-info photo]
  (:size/source (first (filter #(= :medium (:size/label %)) (flickr/photo-sizes flickr-api-info photo)))))

(defn home-page [req flickr-api-info]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body
    (layout
      {}
      (for [photo (photos flickr-api-info)]
        [:img {:src (medium-url flickr-api-info photo)}]))})

(defn routes [flickr-api-info]
  (compojure/routes
    (GET "/" req (home-page req flickr-api-info))))

(defn app-handler [flickr-api-info]
  (-> (routes flickr-api-info)
      (wrap-defaults site-defaults)))
