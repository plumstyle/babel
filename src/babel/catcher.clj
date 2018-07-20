(ns babel.catcher
  (:require [clojure.main :as main]
            [errors.prettify-exception :as p-exc]
            [errors.messageobj :as m-obj]))

;;need to figure out how to get this loaded wihtout having to use this pass through middleware
(defn interceptor
  "applies processor/modify-errors to every response that emerges from the server"
  [handler]
  (fn [inp-message] (identity inp-message)))

;;sets the appropriate flags on the middleware so it is placed correctly
(clojure.tools.nrepl.middleware/set-descriptor! #'interceptor
                                                {:expects #{"eval"} :requires #{} :handles {}})

;;this is the new version down here, using AvisoNovate's method for retrieving errors.
(defn- reset-var!
  [v override]
  (alter-var-root v (constantly override)))

(defn rewrite-error [ex] (println (str  (m-obj/get-all-text (:msg-info-obj (p-exc/process-spec-errors (.getMessage ex)))) "\n")))

;;make the switch

(reset-var! #'main/repl-caught rewrite-error)
