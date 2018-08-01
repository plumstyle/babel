(ns babel.middleware
  (:require [babel.processor :as processor]
            [clojure.tools.nrepl.middleware]
            [clojure.tools.nrepl.transport :as t]))

#_(defn interceptor-old
  "applies processor/modify-errors to every response that emerges from the server"
  [handler]
  (fn [inp-message]
    (let [transport (inp-message :transport)]
      (handler (assoc inp-message :transport
                      (reify Transport
                        (recv [this] (.recv transport))
                        (recv [this timeout] (.recv transport timeout))
                        (send [this msg]     (.send transport (processor/modify-errors msg)))))))))


(defn interceptor
  "applies proccesor/modify-errors to every error object that emerges from the server"
  [handler]
  (fn [inp-message]
    (let [{:keys [session transport]} inp-message]
    (if-let [e (@session #'*e)]
      (do
        (when (get @session #'*e) (println (get @session #'*e)))
        (handler (assoc inp-message :err (str "Babel here:" (.getMessage e)))))
      (handler (assoc inp-message :message "Babel wuz here"))))))

;;sets the appropriate flags on the middleware so it is placed correctly
(clojure.tools.nrepl.middleware/set-descriptor! #'interceptor
                                                {:expects #{"eval"} :requires #{} :handles {}})

(println "babel.middleware loaded")
