(ns times.controller
  (:use compojure.core)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [noir.response :as resp]))

(def ^:dynamic *username* "defaultuser")

(defroutes times-routes
  (GET "/" [] (view/main-page))
  (GET "/projects" [] (view/project-page))
  (POST "/projects/add" [name description] 
        (models/insert-project-of-user name description *username*)
        (resp/redirect "/projects"))
  (GET "/projects/delete/:id" [id]
       (models/delete-project-of-user (clojure.edn/read-string id) *username*)
       (resp/redirect "/projects")))

