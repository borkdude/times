(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time complement])
  (:use (lobos [migration :only [defmigration]] 
               core 
               schema)))

;; to execute migrations:
;; put lobos back in project.clj
;; possibly adapt login settings in db.config
;; start a REPL
;; (use 'lobos.core 'lobos.connectivity 'lobos.migration 'lobos.migrations 'times.config)
;; (open-global (get-db-config))
;; (migrate)
;; or (roll-back)
;; or (roll-back)
;; revert login settings in db.config
;; remove lobos from project.clj
;; more info, see https://github.com/budu/lobos

(defmigration create-foo-table
  (up []
    (create 
      (table :messages
             (integer :id :primary-key :auto-inc)
             (varchar :name 100 :not-null)
             (text :message))))
  (down []
    (drop 
      (table :messages))))

;(defmigration migration-1
;  (up []
;  	(create
;  		(table :user
;    		(integer :id :primary-key :auto-inc)
;    		(varchar :name 100 :not-null)
;      	(varchar :password 100 :not-null))))
;  (down []
;    (drop
;      (table :user))))
;
;(defmigration migration-2
;  (up []
;  	(create
;  		(table :project
;    		(integer :id :primary-key :auto-inc)
;    		(varchar :name 100 :not-null))))
;  (down []
;    (drop
;      (table :project))))
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