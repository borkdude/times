(ns times.controller
  (:use compojure.core)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [noir.response :as resp]))

(def ^:dynamic *username* "defaultuser")

(defn print-pass [x]
  (println x)
  x)

(defroutes times-routes
  (GET "/" [] (view/main-page))
  (GET "/projects" [] (view/project-page))
  (POST "/projects/add" [name description] 
        (models/insert-project-of-user name description *username*)
        (resp/redirect "/projects"))
  (GET "/projects/delete/:id" [id]
       (models/delete-project-of-user (clojure.edn/read-string id) *username*)
       (resp/redirect "/projects"))
  (GET "/weeks" [] (view/week-page))
  (POST "/weeks/add" [& {:keys [weeknr year description budget] :as k}]
        (let [k (assoc k :weeknr (clojure.edn/read-string weeknr))
              k (assoc k :year (clojure.edn/read-string year))
              k (assoc k :budget (clojure.edn/read-string budget))] 
          (do (models/insert-week-of-user (print-pass (assoc k :name *username*))) 
            (resp/redirect "/weeks"))))
   (GET "/weeks/delete/:id" [id]
       (models/delete-week-of-user (clojure.edn/read-string id) *username*)
       (resp/redirect "/weeks")))

