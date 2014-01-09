(ns times.models
  (:use korma.db korma.core times.config))

(defdb db (get-db-config))

;;; helper ;;;
(declare get-userid-by-name)
(defn replace-username-by-userid [m]
  (let [userid (get-userid-by-name (:user m))]
    (assoc m :user userid)))

;;; users ;;;
(defentity users
  #_(has-many projects {:fk :user}))

(defn insert-user [name password]
  (insert users (values {:name name :password password})))

(defn get-userid-by-name [name]
  (:id (first (select users (fields [:id]) (where {:name name})))))

;;; projects ;;;
(defentity projects
  (belongs-to users {:fk :user}))

(defn get-projectname-set-for-user [name]
  (into #{} (map :name (select projects (fields [:name])
                               (with users)
                               (where {:users.name name})))))
(defn get-projects-of-user [name]
  (select projects
          (with users)
          (where {:users.name name})))
;; TODO re-use query that is common to the above two expressions?

(defn insert-project-of-user [{:keys [name description budget user] :as k}]
  (insert projects (values (replace-username-by-userid k))))

(defn delete-project-of-user [id user]
  (delete projects (where {:id id :user (get-userid-by-name user)})))


(defn edit-project-of-user [{:keys [name description budget user oldname] :as k}]
  (update projects 
          (set-fields 
            (dissoc (replace-username-by-userid k) :oldname))
          (where {:name oldname})))
          

;;; weeks ;;;
(defentity weeks)

(defn get-weeks-of-user [name]
  (select weeks
          (join users (= :weeks.user :users.id))
          (where {:users.name name})))

(defn insert-week-of-user [{:keys [weeknr year description budget user] :as k}]
  (insert weeks (values (replace-username-by-userid k))))

(defn delete-week-of-user [id user]
  (delete weeks (where {:id id :user (get-userid-by-name user)})))

;;; activities ;;;
(defentity activities
  (belongs-to weeks {:fk :week})
  (belongs-to projects {:fk :project}))

#_(insert activities (values {:minutes 50 :date (java.sql.Date. 0) :project 5 :week 9 :description "Test activity"}))
;;  Remember that when the primary key is not "id", the table name is not the name of the entity defined or the foreign key is not in the format "tablename_id" you have to define them in the entity.
(defn get-timesheet-of-user [{:keys [user weeknr] :as k}]
  (select activities 
          (join users (= :activities.user :users.id))
          (where {:users.name name}))
  )


                          






