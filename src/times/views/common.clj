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
    (include-css "/bootstrap/css/bootstrap-responsive.min.css")
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
  [:div {:class "navbar navbar-default navbar-fixed-top"}
   [:div.container
    (link-to {:class "brand"} "/" "Times")
    [:div {:class "collapse navbar-collapse"}
     [:ul {:class "nav navbar-nav"}
      [:li (link-to "/projects" "Projects")]
      [:li (link-to "/weeks" "Weeks")]]]]])


;; <!-- Fixed navbar -->
 ;;      <div class="navbar navbar-default navbar-fixed-top" role="navigation">
 ;;        <div class="container">
 ;;          <div class="navbar-header">
 ;;            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
 ;;              <span class="sr-only">Toggle navigation</span>
 ;;              <span class="icon-bar"></span>
 ;;              <span class="icon-bar"></span>
 ;;              <span class="icon-bar"></span>
 ;;            </button>
 ;;            <a class="navbar-brand" href="#">Project name</a>
 ;;          </div>
 ;;          <div class="collapse navbar-collapse">
 ;;            <ul class="nav navbar-nav">
 ;;              <li class="active"><a href="#">Home</a></li>
 ;;              <li><a href="#about">About</a></li>
 ;;              <li><a href="#contact">Contact</a></li>
 ;;              <li class="dropdown">
 ;;                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
 ;;                <ul class="dropdown-menu">
 ;;                  <li><a href="#">Action</a></li>
 ;;                  <li><a href="#">Another action</a></li>
 ;;                  <li><a href="#">Something else here</a></li>
 ;;                  <li class="divider"></li>
 ;;                  <li class="dropdown-header">Nav header</li>
 ;;                  <li><a href="#">Separated link</a></li>
 ;;                  <li><a href="#">One more separated link</a></li>
 ;;                </ul>
 ;;              </li>
 ;;            </ul>
 ;;          </div><!--/.nav-collapse -->
 ;;        </div>
 ;;      </div>



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
