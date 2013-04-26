(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time complement])
  (:use (lobos [migration :only [defmigration]] 
               core 
               schema)))

;; to execute migrations:

;; possibly adapt login settings in db.config
;; lein with-profile lobos repl
;; (use 'lobos.core 'lobos.connectivity 'lobos.migration 'lobos.migrations 'times.config)
;; (open-global (get-db-config))
;; (migrate) or choose one migration: (migrate "create-users")
;; or (rollback) or (rollback "create-users") or (rollback :all) or (rollback 1)
;; revert login settings in db.config
;; more info, see https://github.com/budu/lobos

(defmigration create-users
  (up []
  	(create
  		(table :users
    		(integer :id :primary-key :auto-inc)
    		(varchar :name 100 :not-null :unique)
      	(varchar :password 100 :not-null))))
  (down []
    (drop
      (table :users))))

(defmigration create-projects
  (up []
      (create
        (table :projects
               (integer :id :primary-key :auto-inc)
               (varchar :name 100 :not-null)
               (text :description)
               (integer :user [:refer :users :id] :not-null)
               (unique (table :projects) [:name :user]))))
  (down []
        (drop
          (table :projects))))


(defmigration create-weeks
  (up []
  	(create
  		(table :weeks
    		(integer :id :primary-key :auto-inc)
    		(integer :year :not-null)
      	(integer :weeknr :not-null)
       	(varchar :description 100) ;; 
        (integer :budget)
       	(integer :user [:refer :users :id] :not-null)
        (unique (table :weeks) [:year :weeknr :user]))))
  (down []
    (drop
      (table :weeks))))

(defmigration create-activities
  (up []
  	(create
  		(table :activities
    		(integer :id :primary-key :auto-inc)
    		(integer :minutes :not-null)
      	(date :date :not-null)
       	(varchar :description 150)
        (integer :project [:refer :projects :id] :not-null)
       	(integer :week [:refer :weeks :id] :not-null))))
  (down []
    (drop
      (table :activities))))