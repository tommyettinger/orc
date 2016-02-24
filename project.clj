(defproject orc "1.0.0-SNAPSHOT"
  :description "Simple IRC game bot"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [overtone/at-at "1.0.0"]
                 ]
  :dev-dependencies [[lein-eclipse "1.0.0"]]
  :jvm-opts ["-Dfile.encoding=utf-8"]
  :aot [orc.core]
  :main orc.core)