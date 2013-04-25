(ns times.models
  (:use korma.db korma.core times.config)
  (:require [clojure.string :refer [trim]]))

(defdb db (get-db-config))

;;; users ;;;
(defentity users)

(defn insert-user [name password]
  (insert users (values {:name name :password password})))

(defn get-userid-by-name [name]
  (:id (first (select users (fields [:id]) (where {:name name})))))

;;; projects ;;;
(defentity projects)

(defn get-projects-of-user [name]
  (select projects
          (join users (= :projects.user :users.id))
          (where {:users.name name})))

(defn insert-project-of-user [name description user]
  (let [name (trim name)
        description (trim description)]
    (insert projects (values 
                       {:name name 
                        :description description 
                        :user (get-userid-by-name user)}))))

(defn delete-project-of-user [id user]
  (delete projects (where {:id id :user (get-userid-by-name user)})))

;;; weeks ;;;
(defentity weeks)

(defn get-weeks-of-user [name]
  (select weeks
          (join users (= :weeks.user :users.id))
          (where {:users.name name})))

(defn insert-week-of-user [& {:keys [weeknr year description budget name] :as k}]
  (let [user (get-userid-by-name name)
        k (dissoc (assoc k :user user) :name)]
    (insert weeks (values k))))

(defn delete-week-of-user [id name]
  (delete weeks (where {:id id :user (get-userid-by-name name)})))
  

(defentity activities)
                          






