(ns times.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-js include-css]]
        times.utils)
  (:require
        [times.models :as models]))

(declare top-menu)

(defhtml base [& content]
  (html5
   [:head
    [:title "Welcome to Times"] 
    (include-css "/bootstrap/css/bootstrap.min.css")
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


(defn project-page [] 
  (base 
    [:h1 "Projects"]
    (form-to {:id "addproject"} [:post "/projects/add"]
             [:table.table 
              [:tr 
               [:th "Name"] [:th "Description"] [:th "Budget"] [:th "Action"]]
              [:tr 
               [:td #_{:class "span3"} (text-field  "name")]
               [:td #_{:class "span5"} (text-field  "description" )]
               [:td #_{:class "span5"} (text-field  "budget" )]
               [:td #_{:class "span4"} [:a {:href "javascript: $('#addproject').submit();"} "Create"]]]
     (for [elt (models/get-projects-of-user "defaultuser")]
       [:tr [:td #_{:class "span3"} (:name elt)]
        [:td #_{:class "span5"} (:description elt)]
        [:td #_{:class "span5"} (minutes-to-hourexpr (:budget elt))]
        [:td #_{:class "span4"} (link-to (str "/projects/delete/" (:id elt)) "Delete")]
        ])
     ])))


;;; weeks 
(defn new-week-form []
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
  

(defn modal-new-week []
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

(defn week-page []
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
    


  
