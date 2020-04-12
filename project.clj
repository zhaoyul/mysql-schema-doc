(defproject mysql-schema-doc "0.2.0-SNAPSHOT"
  :description "An example of using an Excel template to build a report."
  :url "https://github.com/zhaoyul/mysql-schema-doc"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/java.jdbc "0.7.11"]
                 [mysql/mysql-connector-java "8.0.16"]
                 [com.infolace/excel-templates "0.3.3"]
                 [org.clojure/clojure "1.10.0"]]
  :main mysql-schema-doc.gen)
