(ns times.handler
  (:use times.controller
        compojure.core)
  (:require [noir.util.middleware :as noir-middleware]
            [compojure.route :as route]
            [ring.middleware.resource :as resource]))

(defroutes app-routes
  (route/resources "/")
  (route/resources "/bootstrap/"
                   {:root "META-INF/resources/webjars/bootstrap/3.0.3/"})
  (route/resources "/jquery/"
                   {:root "META-INF/resources/webjars/jquery/1.9.0/"})
  (route/not-found "Not Found"))

;;append your application routes to the all-routes vector
(def all-routes [times-routes app-routes])

(def app (->  all-routes
           noir-middleware/app-handler
           ;; add your own middlewares here
))
