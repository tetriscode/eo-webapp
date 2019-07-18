(ns {{namespace}}.web.components
    (:require ["@material-ui/core" :as ui]))

(defn- make-cmp [cmp inner]
  [:> cmp inner])

(defn typography [inner]
  (make-cmp ui/Typography inner))

(defn app-bar [children]
  [:> ui/AppBar children])
