(defproject gcd "0.1.0-SNAPSHOT"
  :description "A Clojure implementation of Euclid's binary extended algorithm"
  :url "https://github.com/DavidNorman/gcd"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot gcd.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
