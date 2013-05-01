(ns times.controller
  (:use compojure.core
        times.utils)
  (:require [compojure.core :as compojure]
            [times.views.common :refer [main-page]]
            [times.views.projects :refer [project-page]]
            [times.views.weeks :refer [weeks-page]]
            [times.views.timesheet :refer [timesheet-page]]  
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
  (GET "/projects" [] (project-page {}))
  (POST "/projects/add" [& {:as projectfields}]
        (let [project (decode-project projectfields)]
          (if (vali/valid-new-project? project) 
            (do
              (models/insert-project-of-user (assoc project :user *username*))
              (resp/redirect "/projects"))
            (project-page projectfields))))
  (GET "/projects/delete/:id" [id]
       (models/delete-project-of-user (to-int id) *username*)
       (resp/redirect "/projects"))
  (POST "/projects/edit" [& {:as projectfields}]
        (let [project (decode-project projectfields)]
          (if (vali/valid-edit-project? project)
            (do 
              (models/edit-project-of-user (assoc project :user *username*))
              (resp/redirect "/projects"))
            (project-page projectfields)))))
              

;; weeks
(defn decode-week [{:keys [weeknr year description budget] :as weekfields}]
  (assoc weekfields 
         :weeknr (to-int weeknr)
         :year (to-int year)
         :description (trim description)
         :budget (hourexpr-to-minutes budget)))

(defroutes weeks-routes
  (GET "/weeks" [] (weeks-page {}))
  (POST "/weeks/add" [& {:keys [weeknr year description budget] :as weekfields}]
        (let [week (decode-week weekfields)]
          (if (vali/valid-week? week) 
            (do (models/insert-week-of-user (assoc week :user *username*)) 
              (resp/redirect "/weeks"))
            (weeks-page weekfields))))
  (GET "/weeks/delete/:id" [id]
       (models/delete-week-of-user (to-int id) *username*)
       (resp/redirect "/weeks")))

;; single week overview
(defroutes timesheet-routes
  (GET "/timesheet/:id" [id]
       (timesheet-page id)))

;; all routes
(defroutes times-routes
  (GET "/" [] (main-page))
  project-routes
  weeks-routes
  timesheet-routes)

