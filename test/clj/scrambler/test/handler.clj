(ns scrambler.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [scrambler.handler :refer :all]
            [scrambler.middleware.formats :as formats]
            [muuntaja.core :as m]
            [mount.core :as mount]))

(defn parse-json [body]
  (m/decode formats/instance "application/json" body))

(use-fixtures
  :once
  (fn [f]
    (mount/start #'scrambler.config/env
                 #'scrambler.handler/app)
    (f)))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= 200 (:status response)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response)))))
  
  (testing "not-provided parameters"
    (let [response (app (request :get "/api/scramble?s1=&s2="))]
      (is (= 500 (:status response)))))

  (testing "only first parameter was provided"
    (let [response (app (request :get "/api/scramble?s1=x&s2="))]
      (is (= 500 (:status response)))))

  (testing "only second parameter was provided"
    (let [response (app (request :get "/api/scramble?s1=&s2=y"))]
      (is (= 500 (:status response)))))
  
  (testing "Provided parameters two parameters with match and with whitespaces"
    (let [response (app (request :get "/api/scramble?s1=%20x&s2=x%20%20"))]
      (is (= 200 (:status response)))))
  
  (testing "Provided parameters two parameters with no match"
    (let [response (app (request :get "/api/scramble?s1=x&s2=y"))]
      (is (= 200 (:status response)))))
  
  (testing "Provided parameters two parameters with match"
    (let [response (app (request :get "/api/scramble?s1=xy&s2=yx"))]
      (is (= 200 (:status response))))))


