(ns {{namespace}}.web.views
    (:require
     [reagent.core :as r]
     [re-frame.core :as re-frame]
     [{{namespace}}.web.subs :as subs]
     [{{namespace}}.web.components :as components]
     ["@material-ui/core" :as ui]
     ["@material-ui/icons" :as icon]))

(defn header []
  [:> ui/AppBar {:position "absolute"}
   [:> ui/Toolbar
    [:> ui/IconButton [:> icon/Menu]]]])

(defn drawer []
  [:> ui/Drawer
   [:> ui/IconButton [:> icon/ChevronLeft]]])

(defn main-panel []
  (let [card (re-frame/subscribe [::subs/card])
        this (r/current-component)]
    [:div
     [:> ui/CssBaseline]
     (header)
     (drawer)
     [:div (r/children this)]]))



