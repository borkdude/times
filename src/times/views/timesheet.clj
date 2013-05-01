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
    #_(form-to {:id "addproject"} [:post "/projects/add"]
             [:table.table 
              (table-header-row "Name" "Description" "Budget" "Action")
              ;; input for new row
              [:tr
               ;; :error-key fieldname placeholder content classes
               (validated-input-td {:errorkey :name 
                                    :fieldname "name" 
                                    :placeholder "Java-13-IV2A" 
                                    ;; if oldname is present, we were not adding a new one
                                    :content (if (not oldname) name) 
                                    :classes ["namefield"]})
               [:td (text-field {:class "descriptionfield" 
                                 :placeholder "Java in 2012-2013 to class IV2A"} 
                                "description" 
                                (if (not oldname) description))]
               (validated-input-td {:errorkey :budget 
                                    :fieldname "budget" 
                                    :placeholder "45:00" 
                                    :content (if (not oldname) budget) 
                                    :classes ["budgetfield"]})
               [:td [:a {:class "btn btn-primary" 
                         :href "javascript: $('#addproject').submit();"} 
                     "Create"]]]
              
              ;; rest of table for projects         
              (for [elt (models/get-projects-of-user "defaultuser")]
                [:tr {:id (:name elt)} [:td.name (:name elt)]
                 [:td.description (:description elt)]
                 [:td.budget (minutes-to-hourexpr (:budget elt))]
                 [:td [:div.btn-group 
                       [:button {:class "btn btn-primary dropdown-toggle" :data-toggle "dropdown"} "Action" [:span.caret]]
                       [:ul.dropdown-menu
                        [:li (link-to {:onclick (str "loadEditProject('" (:name elt) "');") :data-toggle "modal" :data-target "#editProject"} "#" "Edit")] 
                        [:li (link-to {:onclick (str "document.location.href='" "/projects/delete/" (:id elt) "';")}"#" "Delete")]]]]
                 ])])))

