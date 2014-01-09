(defproject times "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.4.9"]
                 [compojure "1.1.5"]
                 [ring-server "0.3.1"]
                 [hiccup "1.0.2"]
                 [korma "0.3.0-RC5"]
                 [org.clojars.kbarber/postgresql "9.2-1002.jdbc4"]
                 [org.webjars/bootstrap "3.0.3"]]
  :ring {:handler times.handler/war-handler
         :nrepl {:start? true :port 4555}}
  :profiles {:production
             {:ring
              {:open-browser? false, :stacktraces? false, :auto-reload? false}}
             :lobos
              {:dependencies [[lobos "1.0.0-beta1"]]}} ;;pulls in java jdbc 0.1.1
  :plugins [[lein-ring "0.8.6"]]
  :min-lein-version "2.0.0")
