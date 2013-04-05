(ns times.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-js include-css]])
  (:require
        [times.models :as models]))

(declare top-menu)

(defhtml base [& content]
  (html5
   [:head
    [:title "Welcome to Times"] 
    (include-css "/webjars/bootstrap/2.3.1/css/bootstrap.min.css")
    (include-css "/webjars/bootstrap/2.3.1/css/bootstrap-responsive.min.css")
    (include-css "/css/times.css")
    (include-js "/webjars/bootstrap/2.3.1/js/bootstrap.min.js")]
   [:body 
    (top-menu)
    [:div.container content]]))

(defn top-menu []
  [:div {:class "navbar navbar-fixed-top"}
   [:div#navbar-inner
   (link-to {:class "brand"} "/" "Times")
   [:ul.nav 
    [:li (link-to "/projects" "Projects")]]]])
  
(defn main-page []
  (base
    [:h1 "Times"]
    [:p "Timesheet management for teachers"]))


(defn project-page [] 
  (base 
    [:h1 "Projects"]
    [:table
     [:tr
      [:th "Naam"] [:th "Omschrijving"] [:th "Verwijderen"]]
     (for [elt (models/get-projects-of-user "defaultuser")]
       [:tr [:td (:name elt)]
        [:td (:description elt)]
        [:td (link-to (str "/projects/delete/" (:id elt)) "Delete")]
        ])
     ]
    (form-to [:post "/projects/add"]
             [:p "Nieuw project: " (text-field "name")]
             [:p "Omschrijving:" (text-field "description")]
             (submit-button "Toevoegen"))))
    


  
