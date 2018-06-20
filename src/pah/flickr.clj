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
  {:photo/id (get-in data ["photo" "id"])
   :photo/title (get-in data ["photo" "title" "_content"])
   :photo/description (get-in data ["photo" "description" "_content"])
   :photo/page-url (get (first (filter #(= (get % "type") "photopage") (get-in data ["photo" "urls" "url"]))) "_content")})

(defn photo [api-info photo-id]
  (let [data (request api-info "flickr.photos.getInfo" {"photo_id" photo-id})]
    (make-photo data)))

(defn photoset-photos [api-info {photoset-id :photoset/id}]
  (let [data (request api-info "flickr.photosets.getPhotos" {"photoset_id" photoset-id})
        photos (get-in data ["photoset" "photo"])
        photo-ids (map #(get % "id") photos)]
    (map (partial photo api-info) photo-ids)))

(defn make-size [data]
  {:size/label (keyword (string/replace (string/lower-case (get data "label")) #" " "-"))
   :size/source (get data "source")})

(defn photo-sizes [api-info {photo-id :photo/id}]
  (let [data (request api-info "flickr.photos.getSizes" {"photo_id" photo-id})
        sizes (get-in data ["sizes" "size"])]
    (map make-size sizes)))

(comment
  (def u (user a "jkseeker"))
  (def p (user-photosets a u))
  (def ps (photoset-photos a (first p)))
  (photo-sizes a (first ps)))
