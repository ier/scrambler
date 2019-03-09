(ns scrambler.utils
  (:require [struct.core :as st]))

(defn sanitize
  "Removes from string all characters excepting letters and casts the rest of string characters to lower case"
  [s]
  (-> s
      (clojure.string/replace #"[^A-Za-z]" "")
      (clojure.string/lower-case)))
