(ns times.utils
  (:require clojure.edn
            [clojure.string :refer [trim]]))

(def ^:dynamic *username* "defaultuser")

(defn print-pass [x]
  (println x)
  x)

(defn to-int [thing]
  (cond 
    (integer? thing) 
    thing
    (string? thing)
    (try (java.lang.Integer/parseInt (trim thing))
      (catch Exception e nil))
    :else nil))

#_(defn read-strings [m & keys]
  (reduce #(assoc %1 %2 (clojure.edn/read-string (%1 %2))) m keys))

(defn hourexpr-to-minutes [hourexpr]
  (let [[hourstr minutestr] (clojure.string/split hourexpr #":")
        hours (to-int hourstr)
        minutes (to-int minutestr)]
    (and hours minutes (+ (* hours 60) minutes))))

(defn minutes-to-hourexpr [minutes]
  (let [pad (fn [n] (str (if (< n 10) "0") n))
        hours (pad (quot minutes 60))
        minutes (pad (print-pass (rem minutes 60)))]
    (str hours ":" minutes)))

