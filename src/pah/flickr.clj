(ns pah.flickr
  (:require [clj-http.client :as h]
            [cheshire.core :as json]
            [clojure.string :as string]))

(defn api-info [api-key] {::api-key api-key})

(defn base-params [api-info method]
  {"api_key" (::api-key api-info)
   "method" method
   "format" "json"})

(defn request-params [api-info method extra-params]
  (merge (base-params api-info method) extra-params))

(def api-url "https://api.flickr.com/services/rest/")

(defn request [api-info method params]
  (let [response (h/get api-url {:query-params (request-params api-info method params)})]
    (-> (:body response)
        (string/replace #"^jsonFlickrApi\(" "")
        (string/replace #"\)$" "")
        json/parse-string)))

(defn user [api-info username]
  (let [data (request api-info "flickr.people.findByUsername" {"username" username})]
    {:user/id (get-in data ["user" "id"])}))

(defn make-photoset [data]
  {:photoset/id (get data "id")
   :photoset/title (get-in data ["title" "_content"])
   :photoset/description (get-in data ["description" "_content"])
   :photoset/photos-count (get data "photos")})

(defn user-photosets [api-info {user-id :user/id}]
  (let [data (request api-info "flickr.photosets.getList" {"user_id" user-id})
        photosets (get-in data ["photosets" "photoset"])]
    (map make-photoset photosets)))

(defn make-photo [data]
  (let [photo (get data "photo")
        urls (get-in photo ["urls" "url"])
        photopage-url (first (filter #(= "photopage" (get % "type")) urls))]
    {:photo/id (get photo "id")
     :photo/title (get-in photo ["title" "_content"])
     :photo/description (get-in photo ["description" "_content"])
     :photo/flickr-page-url (get photopage-url "_content")}))

(defn photo [api-info photo-id]
  (let [data (request api-info "flickr.photos.getInfo" {"photo_id" photo-id})]
    (make-photo data)))

(defn photoset-photos [api-info {photoset-id :photoset/id}]
  (let [data (request api-info "flickr.photosets.getPhotos" {"photoset_id" photoset-id})
        photos (get-in data ["photoset" "photo"])
        photo-ids (map #(get % "id") photos)]
    (map (partial photo api-info) photo-ids)))

(defn label-keyword [label]
  (-> label
      (string/lower-case)
      (string/replace #" " "-")
      (keyword)))

(defn make-size [data]
  {:size/label (label-keyword (get data "label"))
   :size/source (get data "source")})

(defn photo-sizes [api-info {photo-id :photo/id}]
  (let [data (request api-info "flickr.photos.getSizes" {"photo_id" photo-id})
        sizes (get-in data ["sizes" "size"])]
    (map make-size sizes)))
