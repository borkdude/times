(ns times.config
  (:require clojure.edn))

(defn get-db-config []
  (clojure.edn/read-string (slurp (clojure.java.io/resource "db.config"))))
