(ns {{namespace}}.http.core
  (:require [com.stuartsierra.component :as component]
            [{{namespace}}.config :as config]
            [{{namespace}}.http.intercept :as intercept]
            [{{namespace}}.http.response :as response]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn pong [_] (response/ok "pong"))

(defn routes
  []
  #{["/api/ping" :get (conj intercept/common-interceptors `pong)]})

(defrecord HttpService [http-config]
  component/Lifecycle
  (start [this]
    (let [routes #(route/expand-routes
                   (clojure.set/union #{}
                                      (routes)))]
      (assoc this :service-def (merge http-config
                                      {:env                    :prod
                                       ::http/routes            routes
                                       ::http/resource-path     "/public"
                                       ::http/type              :jetty
                                       ::http/host              "0.0.0.0"
                                       ::http/port              (get-in config/config [:http :port])
                                       ::http/allowed-origins   {
                                                                 :allowed-origins (constantly true)}
                                       ::http/container-options {:h2c? true
                                                                 :h2   false
                                                                 :ssl? false}}))))
  (stop [this]
    this))

(defn make-http-service-component [config]
  (map->HttpService {:http-config config}))
