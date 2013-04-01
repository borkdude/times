(ns times.controller
  (:use compojure.core)
  (:require [compojure.core :as compojure]
            [times.view :as view]
            [times.models :as models]
            [noir.response :as resp]))

(defn handle-msg [msg]
  (models/insert-message msg)
  (resp/redirect "/"))

(defroutes times-routes
  (GET "/" [] (view/insert-form))
  (POST "/" {msg :params} (handle-msg msg)))

