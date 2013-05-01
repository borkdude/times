(ns times.views.projects
  (:use times.views.common
        times.utils
        hiccup.form
        [hiccup.element :only [link-to]])
  (:require [times.models :as models]))

(defn edit-project-form [{:keys [name description budget oldname] :as project}]
  (form-to {:id "editprojectform" }
           [:post "/projects/edit"]
           [:fieldset
            (text-field {:type "hidden" :id "oldname"} "oldname" oldname)
            [:label "Name"]
            (validated-input-td {:errorkey :editname 
                                 :fieldname "name" 
                                 :placeholder "Java-13-IV2A" 
                                 :content name
                                 :classes ["namefield"]})
            [:label "Description"]
            [:td (text-field {:class "descriptionfield" 
                              :placeholder "Java in 2012-2013 to class IV2A"} 
                             "description" description)]
            [:label "Budget"]
            (validated-input-td {:errorkey :editbudget 
                                 :fieldname "budget" 
                                 :placeholder "45:00" 
                                 :content budget
                                 :classes ["budgetfield"]})]))

(defn modal-edit-project [project]
  [:div {:id "editProject" 
         :class "modal hide fade" 
         :tabindex "-1" 
         :role "dialog" 
         :aria-labelledby "mymodallabel" 
         :aria-hidden "true"} 
   [:div.modal-header 
    [:a {:class "close" :data-dismiss "modal" :aria-hidden "true"} "&times;"]
    [:h3 "Edit project"]]
   [:div.modal-body (edit-project-form project)]
   [:div.modal-footer 
    (link-to {:class "btn" :data-dismiss "modal"} "#" "Close")
    [:a {:id "editproject" :class "btn btn-primary" :href "javascript: $('#editprojectform').submit();"} "Save"]]])

(defn table-header-row [& headers]
  (into [:tr] 
        (for [header headers] [:th header])))

(defn project-page [{:keys [name description budget oldname] :as project}] 
  (base    
    ;; modal editor, shows directly when oldname is present
    (modal-edit-project project)
    (if oldname [:script (str "$('#editProject').modal('show');")])
    
    [:h1 "Projects"]
    (form-to {:id "addproject"} [:post "/projects/add"]
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