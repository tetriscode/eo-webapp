(ns leiningen.new.eo-webapp
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files project-name sanitize-ns]]
            [leiningen.core.main :as main]))

(def render (renderer "eo-webapp"))

(defn render-web [fname data]
  [(str "src/{{sanitized}}/web/" fname) (render (str "web/" fname) data)])

(defn render-http [fname data]
  [(str "src/{{sanitized}}/http/" fname) (render (str "http/" fname) data)])

(def web-files ["components.cljs" "config.cljs" "core.cljs" "db.cljs" "events.cljs" "subs.cljs" "views.cljs"])

(defn eo-webapp
  "FIXME: write documentation"
  [name & args]
  (let [main-ns (sanitize-ns name)
        data {:raw-name name
              :name (project-name name)
              :namespace main-ns
              :sanitized (name-to-path main-ns)}]
    (main/info "Generating fresh 'lein new' eo-webapp project.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["shadow-cljs.edn" (render "shadow-cljs.edn" data)]
             ["deps.edn" (render "deps.edn" data)]
             ["package.json" (render "package.json" data)]
             ["Dockerfile" (render "Dockerfile" data)]
             ["src/{{sanitized}}/server.clj" (render "server.clj" data)]
             ["src/{{sanitized}}/config.clj" (render "config.clj" data)]
             (render-web "components.cljs" data)
             (render-web "config.cljs" data)
             (render-web "core.cljs" data)
             (render-web "db.cljs" data)
             (render-web "events.cljs" data)
             (render-web "subs.cljs" data)
             (render-web "views.cljs" data)
             (render-http "core.clj" data)
             (render-http "response.clj" data)
             (render-http "intercept.clj" data)
             ["config/logback.xml" (render "logback.xml" data)])))
