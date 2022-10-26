(defproject program ""

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.5.648"]

                 [com.formdev/flatlaf "2.1"]
                 [com.formdev/flatlaf-extras "2.1"]
                 [com.miglayout/miglayout-swing "5.3"]]

  :source-paths ["src"]
  :target-path "out"
  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]

  :main Treebeard.main
  :profiles {:uberjar {:aot :all}}
  :repl-options {:init-ns Treebeard.main}

  :uberjar-name ~(str "Treebeard-"
                      (->
                       (clojure.java.shell/sh "git" "rev-parse" "--short" "HEAD")
                       :out
                       (clojure.string/trim-newline))
                      ".jar"))