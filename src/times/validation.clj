(ns times.validation
  (:use times.utils)
  (:require [noir.validation :as vali]
            [times.models :as models]
            clojure.edn))

(defn valid-project?  [{:keys [name description budget]}] 
  (vali/rule (vali/min-length? name 3)
             [:name "Name must be at least 3 characters long."])
  (vali/rule (not ((models/get-projectname-set *username*) name))
             [:name "Project name already taken."])
  (vali/rule (try (hourexpr-to-minutes budget) 
               (catch Exception e nil))
             [:budget "Invalid budget expression, not in the form nn:nn"])
  (not (vali/errors? :name :budget)))

(defn valid-week? [{:keys [weeknr year description budget]}]
  (vali/rule (integer? (clojure.edn/read-string weeknr))
             [:weeknr "Week number is not an integer"])
  (vali/rule (integer? (clojure.edn/read-string year))
             [:year "Year is not an integer"])
  (vali/rule (try (hourexpr-to-minutes budget) 
               (catch Exception e nil))
             [:budget "Invalid budget expression, not in the form nn:nn"])
  (not (vali/errors? :weeknr :year :budget)))
  
  