(ns scrambler.utils
  (:require
   [clojure.string :as str]))

(defn sanitize
  "Removes from string all characters excepting letters and casts the rest of string characters to lower case"
  [s]
  (-> s
      (str/replace #"[^A-Za-z]" "")
      (str/lower-case)))
