(ns times.controller
  (:use compojure.core
        times.utils)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [noir.response :as resp]))

(def ^:dynamic *username* "defaultuser")

#_(defn print-pass [x]
  (println x)
  x)

(defroutes times-routes
  (GET "/" [] (view/main-page))
  (GET "/projects" [] (view/project-page))
  (POST "/projects/add" [name description budget]
        (models/insert-project-of-user name description (hourexpr-to-minutes budget) *username*)
        (resp/redirect "/projects"))
  (GET "/projects/delete/:id" [id]
       (models/delete-project-of-user (clojure.edn/read-string id) *username*)
       (resp/redirect "/projects"))
  (GET "/weeks" [] (view/week-page))
  (POST "/weeks/add" [& {:keys [weeknr year description budget] :as k}]
        (let [k (read-strings k :weeknr :year :budget)] 
          (do (models/insert-week-of-user (assoc k :name *username*)) 
            (resp/redirect "/weeks"))))
  (GET "/weeks/delete/:id" [id]
       (models/delete-week-of-user (clojure.edn/read-string id) *username*)
       (resp/redirect "/weeks")))

