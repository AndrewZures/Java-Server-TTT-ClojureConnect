(defproject org.clojars.andrewzures/ruby_java_clojure_server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[speclj "2.9.0"]]}}
  :plugins [[speclj "2.9.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.andrewzures/java-server "0.2.2-SNAPSHOT"]
                 [org.clojars.andrewzures/ruby_ttt "0.1.3-SNAPSHOT"]
                 [org.clojars.andrewzures/jruby-lein "0.1.0-SNAPSHOT"]]
  :main cloj-server.core
  :java-source-paths ["src/tttmiddleware/interfaces"]
  :test-paths ["spec/"])
