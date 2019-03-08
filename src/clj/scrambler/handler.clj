(ns scrambler.handler
  (:require [scrambler.middleware :as middleware]
            [scrambler.routes.services :refer [service-routes]]
            [scrambler.routes.home :refer [home-routes]]
            [compojure.core :refer [routes wrap-routes]]
            [ring.util.http-response :as response]
            [compojure.route :as route]
            [scrambler.env :refer [defaults]]
            [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(mount/defstate app
  :start
  (middleware/wrap-base
    (routes
     (-> #'home-routes
         (wrap-routes middleware/wrap-csrf)
         (wrap-routes middleware/wrap-formats))
     #'service-routes
     (route/not-found
      "page not found"))))

