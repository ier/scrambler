(ns scrambler.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [scrambler.scramble :as scrmbl]))

(def service-routes
  (api
    {:swagger {:ui "/swagger-ui"
               :spec "/swagger.json"
               :data {:info {:version "1.0.0"
                             :title "Scrambler API"
                             :description "Scrambler services"}}}}

    (context "/api" []
      :tags ["v1"]

      (GET "/scramble" []
        :return       Boolean
        :query-params [s1 :- String, s2 :- String]
        :summary      "Returns true if a portion of s1 characters can be rearranged to match s2, otherwise returns false."
        (ok (scrmbl/scramble? s1 s2))))))
