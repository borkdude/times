(ns times.views.weeks
  (:use times.views.common
        times.utils
        hiccup.form
        [hiccup.element :only [link-to]])
  (:require [times.models :as models]))

;;; weeks 
#_(defn new-week-form []
  (form-to {:id "newweekform" }
           [:post "/weeks/add"]
           [:fieldset
            ;[:legend "Add new week"]
            [:label "Week number"]
            (text-field {:id "formweeknr"} "weeknr")
            [:label "Year"]
            (text-field {:id "formyear"} "year")
            [:label "Budget"]
            (text-field "budget" "32")
            [:label "Description"]
            (text-field "description")
            [:label]
            #_[:button {:class "btn" :type "submit"} "Add"]]))
  

#_(defn modal-new-week []
  [:div {:id "newWeek" 
         :class "modal hide fade" 
         :tabindex "-1" 
         :role "dialog" 
         ;;:aria-labelledby "mymodallabel" 
         :aria-hidden "true"} 
   [:div.modal-header 
    [:h3 "Add new week"]
    [:button {:type "button" :class "close" :data-dismiss "modal" :aria-hidden "true"} "&times;"]]
   [:div.modal-body (new-week-form)]
   [:div.modal-footer 
    (link-to {:class "btn"} "#" "Close")
    (link-to {:id "saveweek" :class "btn btn-primary"} "#" "Save")]])

#_(defn week-page []
  (base 
    [:h1 "Weeks"]
    [:button {:role "button" :class "btn" :data-toggle "modal" :data-target "#newWeek"} "New week"]
    (modal-new-week)
    [:table.table
     [:tr
      [:th "Weeknummer"] [:th "Omschrijving"] [:th "Verwijderen"]]
     (for [elt (models/get-weeks-of-user "defaultuser")]
       [:tr 
        [:td (link-to (str "/weeks/view/" (:id elt)) (str (:weeknr elt) " / " (:year elt)))]
        [:td (:description elt)]
        [:td (link-to (str "/weeks/delete/" (:id elt)) "Delete")]
        ])]
    ))


(defn weeks-page [{:keys [weeknr year budget description ]}] 
  (base 
    [:h1 "Weeks"]
    ;; input for new project
    (form-to {:id "addweek"} [:post "/weeks/add"]
             [:table.table 
              ;; table header
              [:tr 
               [:th "Weeknumber"] [:th "Year"] [:th "Description"] [:th "Budget"] [:th "Action"]]
              [:tr
               (validated-input-td {:errorkey :weeknr 
                                    :fieldname "weeknr" 
                                    :content weeknr 
                                    :classes ["weekfield"]})
               (validated-input-td {:errorkey :year 
                                    :fieldname "year" 
                                    :content year 
                                    :classes ["yearfield"]})
               [:td (text-field {:class "descriptionfield" 
                                 :placeholder "Lectures week 1"} 
                                "description" 
                                description)]
               (validated-input-td {:errorkey :budget 
                                    :fieldname "budget" 
                                    :placeholder "32:00"
                                    :content budget 
                                    :classes ["budgetfield"]})
               [:td [:a {:href "javascript: $('#addweek').submit();"} "Create"]]]
              ;; rest of table for projects         
              (for [elt (models/get-weeks-of-user "defaultuser")]
                [:tr 
                 [:td (link-to (str "/timesheet/" (:weeknr elt)) (:weeknr elt))]
                 [:td (:year elt)]
                 [:td (:description elt)]
                 [:td (minutes-to-hourexpr (:budget elt))]
                 [:td (link-to (str "/weeks/delete/" (:id elt)) "Delete")]
                 ])])))
;; TODO make this form the same as project screen