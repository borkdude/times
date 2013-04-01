(ns times.models
  (:use korma.db korma.core times.config))

(defdb db (get-db-config))

(defentity messages)

#_(select messages)

#_(insert messages
        (values {:name "Michieltje" :message "I was here in the beginning"}))

(defn get-messages []
  (select messages))

(defn insert-message [msg]
  (insert messages 
          (values msg)))
  