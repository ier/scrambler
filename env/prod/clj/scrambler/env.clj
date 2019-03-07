(ns scrambler.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[scrambler started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[scrambler has shut down successfully]=-"))
   :middleware identity})
