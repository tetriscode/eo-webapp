(ns {{namespace}}.web.core
    (:require
     [reagent.core :as reagent]
     [re-frame.core :as re-frame]
     [{{namespace}}.web.events :as events]
     [{{namespace}}.web.views :as views]
     [{{namespace}}.web.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "root")))

(defn ^:export start []
  (dev-setup)
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))

(defn ^:export stop []
  (println "stpping"))

(defn ^:export init []
  (start))
