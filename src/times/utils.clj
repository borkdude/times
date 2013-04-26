(ns times.utils)

(defn print-pass [x]
  (println x)
  x)

(defn read-strings [m & keys]
  (reduce #(assoc %1 %2 (clojure.edn/read-string (%1 %2))) m keys))

(defn hourexpr-to-minutes [hourexpr]
  (let [[hourstr minutestr] (clojure.string/split hourexpr #":")
        hours (clojure.edn/read-string hourstr)
        minutes (clojure.edn/read-string minutestr)]
    (+ (* hours 60) minutes)))

(defn minutes-to-hourexpr [minutes]
  (let [pad (fn [n] (str (if (< n 10) "0") n))
        hours (pad (quot minutes 60))
        minutes (pad (print-pass (rem minutes 60)))]
    (str hours ":" minutes)))

