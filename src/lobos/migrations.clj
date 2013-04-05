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
    		(varchar :name 100 :not-null :unique)
        (text :description)
        (integer :user [:refer :users :id] :not-null))))
  (down []
    (drop
      (table :projects))))
;
;(defmigration migration-3
;  (up []
;  	(create
;  		(table :timesheet
;    		(integer :id :primary-key :auto-inc)
;    		(integer :year :not-null)
;      	(integer :week :not-null)
;       	(varchar :description 100)
;        (varchar :status 100)
;        (integer :contract_time)
;       	(integer :user_id [:refer :user :id] :not-null))))
;  (down []
;    (drop
;      (table :timesheet))))
;
;(defmigration migration-4
;  (up []
;  	(create
;  		(table :hour_row
;    		(integer :id :primary-key :auto-inc)
;    		(integer :minutes :not-null)
;      	(varchar :date 10 :not-null)
;       	(varchar :description 100)
;        (integer :project_id [:refer :project :id] :not-null)
;       	(integer :timesheet_id [:refer :timesheet :id] :not-null))))
;  (down []
;    (drop
;      (table :hour_row))))