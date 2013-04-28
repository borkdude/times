(ns times.models
  (:use korma.db korma.core times.config))

(defdb db (get-db-config))

;;; helper ;;;
(declare get-userid-by-name)
(defn replace-username-by-userid [m]
  (let [userid (get-userid-by-name (:user m))]
    (assoc m :user userid)))

;;; users ;;;
(defentity users)

(defn insert-user [name password]
  (insert users (values {:name name :password password})))

(defn get-userid-by-name [name]
  (:id (first (select users (fields [:id]) (where {:name name})))))

;;; projects ;;;
(defentity projects)

(defn get-projectname-set-for-user [name]
  (into #{} (map :name (select projects (fields [:name])
                               (join users (= :projects.user :users.id))
                               (where {:users.name name})))))

(defn get-projects-of-user [name]
  (select projects
          (join users (= :projects.user :users.id))
          (where {:users.name name})))

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
(defentity activities)


                          






