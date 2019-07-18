(ns {{namespace}}.config)

(def config {:http {:port (if-let [port (System/getenv "PORT")]
                            (Integer/parseInt port)
                            8080)}})

