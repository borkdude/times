(ns times.controller
  (:use compojure.core
        times.utils)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [times.validation :as vali]
            [noir.response :as resp]
            [clojure.string :refer [trim]]))

;; projects
(defn decode-project [{:keys [name description budget] :as projectfields}]
  (assoc projectfields 
         :name (trim name)
         :description (trim description)
         :budget (hourexpr-to-minutes budget)))

(defroutes project-routes
  (GET "/projects" [] (view/project-page {}))
  (POST "/projects/add" [& {:as projectfields}]
        (let [project (decode-project projectfields)]
          (if (vali/valid-project? project) 
            (do
              (models/insert-project-of-user (assoc project :user *username*))
              (resp/redirect "/projects"))
            (view/project-page projectfields))))
  (GET "/projects/delete/:id" [id]
       (models/delete-project-of-user (to-int id) *username*)
       (resp/redirect "/projects")))

;; weeks
(defn decode-week [{:keys [weeknr year description budget] :as weekfields}]
  (assoc weekfields 
         :weeknr (to-int weeknr)
         :year (to-int year)
         :description (trim description)
         :budget (hourexpr-to-minutes budget)))

(defroutes week-routes
  (GET "/weeks" [] (view/week-page {}))
  (POST "/weeks/add" [& {:keys [weeknr year description budget] :as weekfields}]
        (let [week (decode-week weekfields)]
          (if (vali/valid-week? week) 
            (do (models/insert-week-of-user (assoc week :user *username*)) 
              (resp/redirect "/weeks"))
            (view/week-page weekfields))))
  (GET "/weeks/delete/:id" [id]
       (models/delete-week-of-user (to-int id) *username*)
       (resp/redirect "/weeks")))

;; all routes
(defroutes times-routes
  (GET "/" [] (view/main-page))
  project-routes
  week-routes)

