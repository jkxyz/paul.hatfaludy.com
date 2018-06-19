(ns pah.flickr
  (:require [clj-http.client :as h]
            [cheshire.core :as json]
            [clojure.string :as string]))

(defstruct api-info :api-key)

(defn base-params [api-info method]
  {"api_key" (:api-key api-info)
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

(defn photosets [api-info user-id]
  (let [data (request api-info "flickr.photosets.getList" {"user_id" user-id})
        photosets (get-in data ["photosets" "photoset"])]
    (map make-photoset photosets)))

(defn make-photo [data]
  {:photo/id (get data "id")
   :photo/title (get data "title")})

(defn photoset-photos [api-info photoset-id]
  (let [data (request api-info "flickr.photosets.getPhotos" {"photoset_id" photoset-id})
        photos (get-in data ["photoset" "photo"])]
    (map make-photo photos)))
       
(comment
  (def u (user a "jkseeker"))
  (def p (photosets a (:user/id u)))
  (def ps (photoset-photos a (:photoset/id (first p)))))
