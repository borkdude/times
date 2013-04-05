(ns times.models
  (:use korma.db korma.core times.config)
  (:require [clojure.string :refer [trim]]))

(defdb db (get-db-config))

(defentity users)

(defentity projects)
                          
(defn get-projects-of-user [name]
  (select projects
          (join users (= :projects.user :users.id))
          (where {:users.name name})))

(defn insert-user [name password]
  (insert users (values {:name name :password password})))

(defn get-userid-by-name [name]
  (:id (first (select users (fields [:id]) (where {:name name})))))

(defn insert-project-of-user [name description user]
  (let [name (trim name)
        description (trim description)]
    (insert projects (values 
                       {:name name 
                        :description description 
                        :user (get-userid-by-name user)}))))

(defn delete-project-of-user [id user]
  (delete projects (where {:id id :user (get-userid-by-name user)})))