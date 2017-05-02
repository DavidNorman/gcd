(ns gcd.core-test
  (:require [clojure.test :refer :all]
            [gcd.gcd :as g]))

(defn bi [n] (.toBigInteger n))

(deftest powers-of-two-1
  (testing "common power of 2 removal"
    (is (= (g/remove-powers-of-two (bi 2N) (bi 2N))
           {:c 1 :a 1 :b 1}))
    (is (= (g/remove-powers-of-two (bi 7N) (bi 5N))
           {:c 0 :a 7 :b 5}))
    (is (= (g/remove-powers-of-two (bi 10N) (bi 5N))
           {:c 0 :a 10 :b 5}))
    (is (= (g/remove-powers-of-two (bi 10N) (bi 14N))
           {:c 1 :a 5 :b 7}))
    (is (= (g/remove-powers-of-two (bi 20N) (bi 24N))
           {:c 2 :a 5 :b 6}))))

(deftest Rx-is-odd-xform
  (testing "Transform when Rx is odd"

    ; Ry is larger than Rx
    (is (= (g/Rx-is-odd {:c 0
                         :a (bi 5N)
                         :b (bi 7N)
                         :x {:r (bi 5N) :s (bi 2N) :t (bi 5N)}
                         :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}})
           {:c 0
            :a (bi 5N)
            :b (bi 7N)
            :x {:r (bi 5N) :s (bi 2N) :t (bi 5N)}
            :y {:r (bi 2N) :s (bi 2N) :t (bi -3N)}}))

    ; Rx is larger than Ry
    (is (= (g/Rx-is-odd {:c 0
                         :a (bi 11N)
                         :b (bi 7N)
                         :x {:r (bi 11N) :s (bi 4N) :t (bi 1N)}
                         :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}})
           {:c 0
            :a (bi 11N)
            :b (bi 7N)
            :x {:r (bi 4N) :s (bi 0N) :t (bi -1N) }
            :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}}))

  ))


(deftest Rx-is-even-xform
  (testing "Transform when Rx is even"

    ; S and T both even
    (is (= (g/Rx-is-even {:c 0
                          :a (bi 6N)
                          :b (bi 7N)
                          :x {:r (bi 6N) :s (bi 2N) :t (bi 4N)}
                          :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}})
           {:c 0
            :a (bi 6N)
            :b (bi 7N)
            :x {:r (bi 3N) :s (bi 1N) :t (bi 2N)}
            :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}}))

    ; S and T not both even
    (is (= (g/Rx-is-even {:c 0
                          :a (bi 6N)
                          :b (bi 7N)
                          :x {:r (bi 6N) :s (bi 7N) :t (bi 4N)}
                          :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}})
               {:c 0
                :a (bi 6N)
                :b (bi 7N)
                :x {:r (bi 3N) :s (bi 7N) :t (bi -1N)}
                :y {:r (bi 7N) :s (bi 4N) :t (bi 2N)}}))

    ))


(deftest gcd
  (testing "GCD function is correct"
    (let [d [
             ; Basic co-prime, one even
             [[30N 77N] 1]

             ; Basic co-prime, both odd
             [[100N 201N] 1]

             ; Common factor of 2, otherwise co-prime
             [[100N 36N] 4]

             ; Common factor of 2, with extra factor of 2
             [[200N 36N] 4]

             ; Common factor of 2, not co-prime
             [[300N 108N] 12]

             ; Commutative
             [[108N 300N] 12]

             ; Both prime
             [[5N 9N] 1]

             ; One 1
             [[1N 10N] 1]

             ; Both 1
             [[1N 1N] 1]

             ; Both same
             [[10N 10N] 10]

             ; One is a factor of the other
             [[5N 10N] 5]

             ]
          res (map #(g/gcd (first (first %)) (second (first %))) d)
          zipped (map vector res d)]

       ; GCD correct
       (is (= (map :gcd res)
              (map second d)))

       ; Weights are valid
       (is (= (map #(+
                      (* (:s (first %)) (reduce get (second %) [0 0]))
                      (* (:t (first %)) (reduce get (second %) [0 1]))) zipped)
              (map second d))))))
