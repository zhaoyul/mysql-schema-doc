(ns mysql-schema-doc.gen
  (:require
   [clojure.java.jdbc :as j]
   [excel-templates.build :as excel]))



(def mysql-db {:dbtype "mysql"
               :dbname "information_schema"
               :user "root"
               :password "your-password"})
(defn tables []
  (j/query mysql-db ["select table_name, engine, table_comment from tables where table_schema = 'a';"]))

(defn get-col-info [table-name]
  (j/query mysql-db ["select COLUMN_NAME, COLUMN_DEFAULT, ORDINAL_POSITION, IS_NULLABLE, DATA_TYPE, COLUMN_TYPE, COLUMN_KEY, COLUMN_COMMENT from COLUMNS where TABLE_SCHEMA='a' and table_name = ?" table-name]))


(defn gen-table-data [t-name]
  (->> t-name
       get-col-info
       (map vals)
       (map (fn [e] (vec (conj e nil))))
       (sort-by (fn [v] (get v 2)))))

(defn get-all-table-name []
  (->> (tables)
       (map :table_name )))


(defn individual-table-sheet-lst []
  (for [table-name (get-all-table-name)]
    {:sheet-name table-name
     4 (gen-table-data table-name)}
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

#_(create-schema-doc)
