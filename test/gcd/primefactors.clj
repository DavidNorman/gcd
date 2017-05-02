(ns gcd.primefactors
  "Test utility for finding prime factors:
  http://stackoverflow.com/questions/9556393/clojure-tail-recursion-with-prime-factors")

(defn primefactors
  ([n]
    (primefactors n 2 '()))
  ([n candidate acc]
    (cond (<= n 1) (reverse acc)
          (zero? (rem n candidate)) (recur (/ n candidate) candidate (cons candidate acc))
          :else (recur n (inc candidate) acc))))
