(ns times.validation
  (:use times.utils)
  (:require [noir.validation :as vali]
            [times.models :as models]
            clojure.edn))

(def invalid-budget-expression "Invalid budget expression, not in the form hh:mm")

(defn valid-project?  [{:keys [name description budget]}] 
  (vali/rule (vali/min-length? name 3)
             [:name "Name must be at least 3 characters long."])
  (vali/rule (not ((models/get-projectname-set-for-user *username*) name))
             [:name "Project name already taken."])
  (vali/rule budget ;; budget has been parsed correctly, else it will be nil
             [:budget invalid-budget-expression])
  (not (vali/errors? :name :budget)))

(defn valid-week? [{:keys [weeknr year description budget]}]
  (vali/rule (and weeknr (>= weeknr 1) (<= weeknr 53))
             [:weeknr "Week number is not an integer or not within range 1-53"])
  (vali/rule year
             [:year "Year is not an integer"])
  (vali/rule budget
             [:budget invalid-budget-expression])
  (not (vali/errors? :weeknr :year :budget)))
  
  