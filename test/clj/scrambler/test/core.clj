(ns scrambler.test.core
  (:require
   [clojure.test :refer :all]
   [scrambler.scramble :as scrmbl]
   [scrambler.utils :as utils]))

(deftest correct-input
  (testing "Correct and naive imputs testing"
    (let [s1 "rkqodlw"
          s2 "world"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "cedewaraaossoqqyt"
          s2 "cedewaraaossoqqyt"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "katas"
          s2 "steak"
          expected false]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "scriptjavx"
          s2 "javascript"
          expected false]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "javascript"
          s2 "javascript"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "scriptsjava"
          s2 "javascripts"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "javscripts"
          s2 "javascript"
          expected false]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "aabbcamaomsccdd"
          s2 "commas"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "commas"
          s2 "commas"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))
    (let [s1 "sammoc"
          s2 "commas"
          expected true]
      (is (= expected (scrmbl/scramble? s1 s2))))))

(deftest incorrect-input
  (testing "Empty inputs testing"
    (let [s1 ""
          s2 ""]
      (is (thrown? java.lang.AssertionError (scrmbl/scramble? s1 s2))))
    (let [s1 "1"
          s2 ""]
      (is (thrown? java.lang.AssertionError (scrmbl/scramble? s1 s2))))
    (let [s1 ""
          s2 "2"]
      (is (thrown? java.lang.AssertionError (scrmbl/scramble? s1 s2)))))

  (testing "Inputs with spaces testing"
    (let [s1 "katas"
          s2 "     "]
      (is (thrown? java.lang.AssertionError (scrmbl/scramble? s1 s2))))
    (let [s1 "     "
          s2 "katas"]
      (is (thrown? java.lang.AssertionError (scrmbl/scramble? s1 s2)))))

  (testing "Sanitize function testing"
    (let [s "aAb  bca#mao m$sC.cdd"
          expected "aabbcamaomsccdd"]
      (is (= expected (utils/sanitize s))))

    (let [s "coM mas      "
          expected "commas"]
      (is (= expected (utils/sanitize s))))
    
    (let [s "s crIp^tJa  v$X"
          expected "scriptjavx"]
      (is (= expected (utils/sanitize s))))

    (let [s " jaёvA}sc  ript      "
          expected "javascript"]
      (is (= expected (utils/sanitize s))))))
