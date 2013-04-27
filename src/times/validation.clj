(ns times.validation
  (:use times.utils)
  (:require [noir.validation :as vali]
            [times.models :as models]))

(defn valid-project?  [{:keys [name description budget]}] 
  (vali/rule (vali/min-length? name 3)
             [:name "Name must be at least 3 characters long."])
  (vali/rule (not ((models/get-projectname-set) name))
             [:name "Project name already taken."])
  (vali/rule (try (hourexpr-to-minutes budget) 
               (catch Exception e nil))
             [:budget "Invalid budget expression, not in the form nn:nn"])
  (not (vali/errors? :name :budget)))