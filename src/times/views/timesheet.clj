(ns times.views.timesheet
  (:use times.views.common
        times.utils
        hiccup.form
        [hiccup.element :only [link-to]])
  (:require [times.models :as models]))

#_((integer :id :primary-key :auto-inc)
    (integer :minutes :not-null)
    (date :date :not-null)
    (varchar :description 150)
    (integer :project [:refer :projects :id] :not-null)
    (integer :week [:refer :weeks :id] :not-null))

(defn timesheet-page [id] 
  (base        
    [:h1 "Timesheet"]
    [:p "Todo"]
    (models/get-projects-of-user "defaultuser")))


#_(table :activities
    		(integer :id :primary-key :auto-inc)
    		(integer :minutes :not-null)
      	(date :date :not-null)
       	(varchar :description 150)
        (integer :project [:refer :projects :id] :not-null)
       	(integer :week [:refer :weeks :id] :not-null))
   

