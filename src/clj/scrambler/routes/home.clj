(ns scrambler.routes.home
  (:require
   [scrambler.layout :as layout]
   [compojure.core :refer [defroutes GET POST]]
   [ring.util.http-response :as response]
   [clojure.java.io :as io]))

(defn home-page
  [request]
  (layout/render request "home.html"))

(defroutes home-routes
  (GET "/" request (home-page request)))
