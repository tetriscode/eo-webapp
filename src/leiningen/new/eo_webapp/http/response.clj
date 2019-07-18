(ns {{namespace}}.http.response
  (:require [clojure.tools.logging :as log]))

(defn is-error? [status]
  (< 300 status))

(defn make-response
  "Creates a response map using the status, body, and headers."
  [status body & {:as headers}]
  (let [res-body (assoc {}
                        :result (if (is-error? status) nil body)
                        :error (if (is-error? status) body nil))]
    (let [res {:status  status
               :body    res-body
               :headers headers}]
      (log/trace "Returning response" res)
      res)))

(def ok (partial make-response 200))
(def created (partial make-response 201))
(def accepted (partial make-response 202))
(def bad-request (partial make-response 400))
(def unauthorized (partial make-response 401))
(def forbidden (partial make-response 403))
(def not-found (partial make-response 404))
(def conflict (partial make-response 409))
(def error (partial make-response 500))
(def unavailable (partial make-response 503))
