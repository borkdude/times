(ns times.config
  (:require [clojure.edn :as edn]))

(defn get-db-config []
  (clojure.edn/read-string (slurp "resources/db.config")))
