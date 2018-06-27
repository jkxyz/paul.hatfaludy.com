(ns pah.pages.home
  (:require [pah.pages.core :refer [layout root-path]]
            [ring.util.response :refer [response content-type]]
            [flickr.user :as fuser]
            [flickr.photoset :as photoset]
            [flickr.photo :as photo]))

(defn get-photosets [flickr]
  (let [user (fuser/find-by-username flickr "jkseeker")
        photosets (photoset/get-by-user flickr user)
        photos (map #(photo/get-by-photoset flickr %) photosets)
        sizes (map #(map (fn [p] (photo/get-sizes flickr p)) %) photos)]
    (for [[photoset photos sizes] (map vector photosets photos sizes)]
      {:title (:flickr.photoset/title photoset)
       :photos (for [[photo sizes] (map vector photos sizes)]
                 {:title (:flickr.photo/title photo)
                  :src (:medium sizes)})})))

(def photosets '({:title "Selected Photographs", 
                  :photos ({:title "Passions Meet", 
                            :src "https://farm2.staticflickr.com/1762/42924765572_50c8f2a9cb.jpg"} 
                           {:title "Route", 
                            :src "https://farm2.staticflickr.com/1838/42924765442_f93c9e8bd9.jpg"} 
                           {:title "Herd", 
                            :src "https://farm2.staticflickr.com/1828/42924765242_0813382837.jpg"} 
                           {:title "Morning Due", 
                            :src "https://farm1.staticflickr.com/900/42924765162_63acd1b60a.jpg"})}))

(defn render [photosets]
  (layout
    {:title "Hatfaludy Paul-Alin – Photographer in Oradea"}
    [:div.message
     [:p
      [:strong "Hey Paul, this site is just for you. "]
      "Your own bespoke site for whatever you want, but I thought that photography was a
      great place to start. You can add new photos using Flickr and they'll automatically 
      show up here. I hope you like it so far; I had fun making it."]
     [:p.message-signed "— Charles"]]
    (for [{:keys [title photos]} photosets]
      [:div.photoset
       [:h3.photoset-title title]
       [:div.photoset-photos
        (for [{:keys [title src]} photos]
          [:a.photo {:href src
                     :title title}
           [:img {:src src
                  :alt title}]])]])))

(defn handle [request flickr]
  (-> (response (render photosets))
      (content-type "text/html")))
