(ns babel.middleware
  (:require [babel.processor :as processor]
            [clojure.tools.nrepl.middleware]
            [clojure.tools.nrepl.transport :as t])
  (:use [clojure.tools.nrepl.misc :only (response-for)]))

(defn interceptor
  "applies processor/modify-errors to every response that emerges from the server"
  [handler]
  (fn [inp-message]
    (let [transport (inp-message :transport)]
      (handler (assoc inp-message :transport
                      (reify t/Transport
                        (recv [this] (.recv transport))
                        (recv [this timeout] (.recv transport timeout))
                        (send [this msg]     (.send transport (processor/modify-errors msg)))))))))
                        ;(send [this msg]     (.send transport msg))))))))

;;sets the appropriate flags on the middleware so it is placed correctly
(clojure.tools.nrepl.middleware/set-descriptor! #'interceptor
                                                {:expects #{"eval"} :requires #{} :handles {}})

(println "babel.middleware loaded")
