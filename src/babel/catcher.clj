(ns babel.catcher
  (:require [clojure.main :as main]
            [errors.prettify-exception :as p-exc]
            [errors.messageobj :as m-obj]))


;;this is the new version down here, using AvisoNovate's method for retrieving errors.
(defn- reset-var!
  [v override]
  (alter-var-root v (constantly override)))

;;demonstration that we can dispatch errors to our processing system. Fails ludicrously, but can likely be adjusted.
#_(defn rewrite-error [ex] (println (str "HORP" (m-obj/get-all-text (:msg-info-obj (p-exc/process-spec-errors (.getMessage ex)))) "\n")))

;;this iteration showed that we cannot just rethrow an exception without going comedically recursive. Again.
#_(defn rewrite-error [ex] (throw (Exception. (str "This is an " (.getMessage ex) ". The stacktrace goes from "(first (.getStackTrace ex)) " to " (first (.getStackTrace ex))))))

(defn rewrite-error [ex] (binding [*out* *err*] (println (str "This is a " (.getMessage ex) " error. The stacktrace goes from "(first (.getStackTrace ex)) " to " (first (.getStackTrace ex))))))

;;make the switch
(reset-var! #'main/repl-caught rewrite-error)
(println "Error messages are handled by babel now")
