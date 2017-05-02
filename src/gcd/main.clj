(ns gcd.main
  (:require [gcd.gcd :as g])
  (:gen-class))

(defn -main
  "Take 2 numbers and find the GCD and the two weights such that
   GCD = S.a + T.b"
  [a b & _]
  (println "GCD " (g/gcd (bigint a) (bigint b))))
