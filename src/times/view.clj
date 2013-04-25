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
    (include-css "/bootstrap/css/bootstrap.min.css")
    (include-css "/bootstrap/css/bootstrap-responsive.min.css")
    (include-css "/css/times.css")
    (include-js "/bootstrap/js/bootstrap.min.js")]
   [:body 
    (top-menu)
    [:div.container content]]))

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
    [:p "Timesheet management for teachers"]))


(defn project-page [] 
  (base 
    [:h1 "Projects"]
    [:table.table
     [:tr
      [:th "Naam"] [:th "Omschrijving"] [:th "Verwijderen"]]
     (for [elt (models/get-projects-of-user "defaultuser")]
       [:tr [:td (:name elt)]
        [:td (:description elt)]
        [:td (link-to (str "/projects/delete/" (:id elt)) "Delete")]
        ])
     ]
    (form-to [:post "/projects/add"]
             [:fieldset
              [:legend "Nieuw project toevoegen"]
              [:label "Projectcode (uniek)"]
              (text-field "name")
              [:label "Omschrijving"]
              (text-field "description")
              [:label]
              [:button {:class "btn" :type "submit"} "Toevoegen"]])))

(defn week-page []
  (base 
    [:h1 "Weeks"]
    [:table.table
     [:tr
      [:th "Weeknummer"] [:th "Omschrijving"] [:th "Verwijderen"]]
     (for [elt (models/get-weeks-of-user "defaultuser")]
       [:tr 
        [:td (link-to (str "/week/view/" (:id elt)) (str (:weeknr elt) " / " (:year elt)))]
        [:td (:description elt)]
        [:td (link-to (str "/week/delete/" (:id elt)) "Delete")]
        ])]
    ))
    


  
