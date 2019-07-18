(ns {{namespace}}.server
    (:gen-class) ; for -main method in uberjar
    (:require [io.pedestal.http :as server]
              [io.pedestal.http.route :as route]
              [{{namespace}}.http.core :as http]
              [clojure.tools.logging :as log]
              [com.stuartsierra.component :as component]))

(defrecord StoreServer
           [http-service]
  component/Lifecycle
  (start [component]
    (log/info "Starting Store Server Component")
    (let [server (server/create-server (:service-def http-service))]
      (server/start server)
      (assoc component :http-server server)))
  (stop [component]
    (log/info "Stopping Store Server Component")
    (update-in component [:http-server] server/stop)))

(defn new-store-server []
  (map->StoreServer {}))

(defn make-store-api-server [config-options]
  (log/debug "Making server config with these options " config-options)
  (let [{:keys [http-config]} config-options]
    (component/system-map
     :http-service (http/make-http-service-component http-config)
     :server (component/using (new-store-server) [:http-service]))))

(defn -main
  "The entry-point for 'lein run'"
  [& _]
  (log/info "Configuring Store API server...")
  ;; create global uncaught exception handler so threads don't silently die
  (Thread/setDefaultUncaughtExceptionHandler
   (reify Thread$UncaughtExceptionHandler
     (uncaughtException [_ thread ex]
       (log/error ex "Uncaught exception on thread" (.getName thread)))))
  (component/start-system
   (make-store-api-server {:http-config {}})))

(def system-val nil)

(defn init-dev []
  (log/info "Initializing dev system for repl")
  (make-store-api-server {:http-config {:env                    :dev
                                        ::server/join?           false
                                        ::server/allowed-origins ["localhost:8080","florist.appspot.com"]}}))

(defn init []
  (alter-var-root #'system-val (constantly (init-dev))))

(defn start []
  (alter-var-root #'system-val component/start-system))

(defn stop []
  (alter-var-root #'system-val
                  (fn [s] (when s (component/stop-system s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (go))

