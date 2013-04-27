(ns times.controller
  (:use compojure.core
        times.utils)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [noir.response :as resp]
            [noir.validation :as vali]))

(def ^:dynamic *username* "defaultuser")

#_(defn print-pass [x]
  (println x)
  x)

(defn valid?  [{:keys [name description budget]}] 
  (vali/rule (vali/min-length? name 3)
             [:name "Name must be at least 3 characters long, you stupid."])
  (not (vali/errors? :name)))

(defroutes times-routes
  (GET "/" [] (view/main-page))
  (GET "/projects" [] (view/project-page {}))
  (POST "/projects/add" [& {:as project}]
        (if (valid? (print-pass project)) 
          (do
            (models/insert-project-of-user (:name project) (:description project) (hourexpr-to-minutes (:budget project)) *username*)
            (resp/redirect "/projects"))
          (view/project-page project)))
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

