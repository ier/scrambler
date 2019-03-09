(ns scrambler.scramble
  (:require [clojure.string :as str]))

(defn- custom-contains?
  "Returns true if set contains element with value not exceeding target item value."
  [xs [k v]]
  (let [x (get xs k)]
    (not (neg? (compare x v)))))

(defn scramble?
  "Returns true if a portion of s1 characters can be rearranged to match s2, otherwise returns false.
   Both parameters are mandatory. java.lang.AssertionError will be thrown in case of pre-check vialation."
  [s1 s2]
  {:pre [(and (pos? (count (str/trim s1)))
              (pos? (count (str/trim s2))))]}
  (let [frq1 (frequencies s1)
        frq2 (frequencies s2)
        occurrences (map #(custom-contains? frq1 %) frq2)]
    (every? true? occurrences)))