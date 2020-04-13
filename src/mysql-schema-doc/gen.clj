(ns mysql-schema-doc.gen
  (:require
   [clojure.java.jdbc :as j]
   [excel-templates.build :as excel]))



(def mysql-db {:dbtype "mysql"
               :dbname "information_schema"
               :user "root"
               :password ""})
(defn tables []
  (j/query mysql-db ["select table_name, table_comment from tables where table_schema = 'd';"]))

(defn get-col-info [table-name]
  (j/query mysql-db ["select COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT from COLUMNS where TABLE_SCHEMA='d' and table_name = ?" table-name]))


(defn gen-table-data [t-name]
  (->> t-name
       get-col-info
       (map vals)
       (map (fn [e] (vec (conj e nil))))
       (sort-by (fn [v] (get v 2)))))

(defn get-all-table-name []
  (->> (tables)
       (map #(select-keys % [:table_name :table_comment]) )))


(defn individual-table-sheet-lst []
  (for [table-info (get-all-table-name)]
    {:sheet-name (:table_name table-info)
     1     [[nil "表名" (:table_name table-info)]]
     2     [[nil "描述" (:table_comment table-info)]]
     4 (gen-table-data (:table_name table-info))}
    ))
(defn report-data []
  {"tables" {5 (->> (tables)
                    (map (fn [m] (conj (vals m) nil)) )
                    vec)}
   "table" (individual-table-sheet-lst) })


(defn create-schema-doc []
  (excel/render-to-file
   "schema-template.xlsx"
   "/tmp/schema.xlsx"
   (report-data)))

;; 调用如下函数在/tmp 目录下生成schema.xlsl文件

(create-schema-doc)
