(ns times.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-js include-css]]
        times.utils)
  (:require
        [times.models :as models]
        [noir.validation :as vali]))

(declare top-menu)

(defhtml base [& content]
  (html5
   [:head
    [:title "Welcome to Times"] 
    (include-css "/bootstrap/css/bootstrap.css")
    (include-css "/bootstrap/css/bootstrap-responsive.min.css")
    (include-css "/css/times.css")
    
    (include-js "/jquery/jquery.min.js") ;; always include jquery before bootstrap!
    (include-js "/bootstrap/js/bootstrap.js")
    ]
   [:body 
    (top-menu)
    [:div.container content]]
   (include-js "/js/times.js") ;;include my own js after elements are created
   ))

(defn top-menu []
  [:div {:class "navbar navbar-inverse navbar-fixed-top"}
   [:div.navbar-inner
    [:div.container
   (link-to {:class "brand"} "/" "Times")
   [:ul.nav 
    [:li (link-to "/projects" "Projects")]
    [:li (link-to "/weeks" "Weeks")]]]]])
  
(defn main-page []
  (base
    [:h1 "Times"]
    [:p "Time tracking for projects."]))

(defn make-vali-class [error-key & classes]
  (let [classes (if (vali/on-error error-key first) (conj classes "error") classes)]
    (apply str (interpose " " classes))))

(defn make-validated-input [error-key fieldname placeholder content & classes]
  [:td (text-field 
         ;; classes, with or without "error", depending on input
         {:class (apply make-vali-class error-key classes) :placeholder placeholder}
         ;; field name          
         fieldname
         ;; content
         content) 
   ;; optional error message
   (if-let [err (vali/on-error error-key first)] [:p.error err])])

(defn project-page [{:keys [name description budget]}] 
  (base 
    [:h1 "Projects"]
    ;; input for new project
    (form-to {:id "addproject"} [:post "/projects/add"]
             [:table.table 
              ;; table header
              [:tr 
               [:th "Name"] [:th "Description"] [:th "Budget"] [:th "Action"]]
              [:tr
               (make-validated-input :name "name" "Java-13-IV2A" name "namefield")
               [:td (text-field {:class "descriptionfield" :placeholder "Java in 2012-2013 to class IV2A"} "description" description)]
               (make-validated-input :budget "budget" "45:00" budget "budgetfield")
               [:td [:a {:href "javascript: $('#addproject').submit();"} "Create"]]]
              ;; rest of table for projects         
              (for [elt (models/get-projects-of-user "defaultuser")]
                [:tr [:td #_{:class "span3"} (:name elt)]
                 [:td #_{:class "span5"} (:description elt)]
                 [:td #_{:class "span5"} (minutes-to-hourexpr (:budget elt))]
                 [:td #_{:class "span4"} (link-to (str "/projects/delete/" (:id elt)) "Delete")]
                 ])])))

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


(defn week-page [{:keys [weeknr year budget description ]}] 
  (base 
    [:h1 "Weeks"]
    ;; input for new project
    (form-to {:id "addweek"} [:post "/weeks/add"]
             [:table.table 
              ;; table header
              [:tr 
               [:th "Weeknumber"] [:th "Year"] [:th "Description"] [:th "Budget"] [:th "Action"]]
              [:tr
               (make-validated-input :weeknr "weeknr" "" weeknr "weekfield")
               (make-validated-input :year "year" "" year "yearfield")
               [:td (text-field {:class "descriptionfield" :placeholder "Lectures week 1"} "description" description)]
               (make-validated-input :budget "budget" "32:00" budget "budgetfield")
               [:td [:a {:href "javascript: $('#addweek').submit();"} "Create"]]]
              ;; rest of table for projects         
              (for [elt (models/get-weeks-of-user "defaultuser")]
                [:tr 
                 [:td (:weeknr elt)]
                 [:td (:year elt)]
                 [:td (:description elt)]
                 [:td (minutes-to-hourexpr (:budget elt))]
                 [:td (link-to (str "/weeks/delete/" (:id elt)) "Delete")]
                 ])])))
    


  
