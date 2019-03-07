(ns user
  (:require [scrambler.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [scrambler.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'scrambler.core/repl-server))

(defn stop []
  (mount/stop-except #'scrambler.core/repl-server))

(defn restart []
  (stop)
  (start))


