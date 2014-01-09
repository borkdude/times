(ns times.views.common
  (:use hiccup.form
        [hiccup.def :only [defhtml]]
        [hiccup.element :only [link-to]]
        [hiccup.page :only [html5 include-js include-css]]
        times.utils)
  (:require
    [noir.validation :as vali]))

(declare top-menu)

(defhtml base [& content]
  (html5
   [:head
    [:title "Welcome to Times"]
    (include-css "/bootstrap/css/bootstrap.css")
    #_(include-css "/bootstrap/css/bootstrap-responsive.css")
    (include-css "/css/times.css")

    (include-js "/jquery/jquery.min.js") ;; always include jquery before bootstrap!
    (include-js "/bootstrap/js/bootstrap.js")
    (include-js "/js/times.js") ;;include my own js after elements are created
    ]
   [:body
    (top-menu)
    [:div.container content]]
   ))

(defn top-menu []
  [:div {:class "navbar navbar-default"}
   [:div.container
    [:div.navbar-header (link-to {:class "navbar-brand"} "/" "Times")]
    [:div {:class "collapse navbar-collapse"}
     [:ul {:class "nav navbar-nav"}
      [:li (link-to "/projects" "Projects")]
      [:li (link-to "/weeks" "Weeks")]]]]])

(defn main-page []
  (base
    [:h1 "Times"]
    [:p "Time tracking for projects."]))

(defn vali-class [error-key classes]
  (let [classes (if (vali/on-error error-key first) (conj classes "error") classes)]
    (apply str (interpose " " classes))))

(defn validated-input-td [{:keys [errorkey fieldname placeholder content classes]}]
  [:td (text-field
         {:class (vali-class errorkey classes) :placeholder placeholder}
         fieldname
         content)
   ;; optional error message
   (if-let [err (vali/on-error errorkey first)] [:p.error err])])
