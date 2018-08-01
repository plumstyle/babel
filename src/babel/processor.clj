(ns babel.processor
  (:require  [clojure.tools.nrepl :as repl]
             [clojure.spec.test.alpha :as stest]
             [errors.messageobj :as m-obj]
             [errors.prettify-exception :as p-exc]))

(defn extract-error "takes an nREPL message and extracts the most recent error object, or nil if the message is not relevant" [inp-message]
;;if we have both an :op 'eval' message and the exception key exists, we can assume that an error has been caused
  (let [{:keys [transport op ex session]} inp-message]
    (if (and (= op "eval") ex)
      (let [e (get @session #'*e)]
        (if e e "Something is wrong")
        nil))))

(defn modify-errors "takes a nREPL response, and returns a message with the errors fixed"
  [inp-message]
  (if (contains? inp-message :err)
    (let [e (extract-error inp-message)]
      (assoc inp-message :err e))
    inp-message))
;  (if (contains? inp-message :err)
;    ;;replace the assoced value with a function call as needed.
;    (assoc inp-message :err (m-obj/get-all-text (:msg-info-obj (p-exc/process-spec-errors (inp-message :err)))))
;    ;(assoc inp-message :err (str (inp-message :err))) ;; Debugging
;    inp-message))

(println "babel.processor loaded")
