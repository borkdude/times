(ns times.view
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-js include-css]])
  (:require
        [times.models :as models]))

(defhtml base [& content]
  (html5
   [:head
    [:title "Welcome to Times"]
    #_(include-css "/css/screen.css")]
   [:body [:div#content content]]))

(defn messages [msgs]
  (for [msg msgs]
    [:p (:name msg) " says " (:message msg)]))
  
(defn insert-form []
  (base
    [:h1 "Insert something"]
    [:p "Yes, really insert something"]
 
    ;here we call our show-guests function
    ;to generate the list of existing comments
    #_(show-guests)
    [:hr]
    [:p (messages (models/get-messages))]
    (form-to [:post "/"]
      [:p "Name:" (text-field "name")]
      [:p "Message:" (text-area {:rows 10 :cols 40} "message")]
      (submit-button "Insert"))))


  
