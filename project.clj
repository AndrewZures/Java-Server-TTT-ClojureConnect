(defproject cloj_server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[speclj "2.7.0"]]}}
  :plugins [[speclj "2.7.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [java-server "0.1.5-SNAPSHOT"]
                 [ruby_ttt "0.1.3-SNAPSHOT"]
                 [jruby-lein "0.1.0-SNAPSHOT"]]
  :main cloj-server.core
  :test-path "spec/"
  )