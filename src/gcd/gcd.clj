(ns gcd.gcd
  "Find the greatest common factor using the binary version
   of the Euclid extended algorithm.  This algorithm uses only
   shifts, adds and subtracts.")

(defn all-even?
  [s]
  (not
    (reduce #(or %1 %2) (map #(.testBit % 0) s))))

(defn remove-powers-of-two
  [a b]
  (loop [c 0
         x a
         y b]
  (if (all-even? [x y])
    (recur (inc c) (.shiftRight x 1) (.shiftRight y 1))
   {:c c :a x :b y})))

(defn Rx [m] (get-in m [:x :r]))
(defn Ry [m] (get-in m [:y :r]))
(defn Sx [m] (get-in m [:x :s]))
(defn Sy [m] (get-in m [:y :s]))
(defn Tx [m] (get-in m [:x :t]))
(defn Ty [m] (get-in m [:y :t]))
(defn A  [m] (:a m))
(defn B  [m] (:b m))


(defn Rx-is-even
  [m]
  (assoc m
    :x
    (if (all-even? [(Sx m) (Tx m)])
      {:r (.shiftRight (Rx m) 1)
       :s (.shiftRight (Sx m) 1)
       :t (.shiftRight (Tx m) 1)}
      {:r (.shiftRight (Rx m) 1)
       :s (.shiftRight (.add (Sx m) (B m)) 1)
       :t (.shiftRight (.subtract (Tx m) (A m)) 1)})))


(defn Rx-is-odd
  [m]
  (if (> (Rx m) (Ry m))
    (assoc m :x
      {:r (.subtract (Rx m) (Ry m))
       :s (.subtract (Sx m) (Sy m))
       :t (.subtract (Tx m) (Ty m))})
    (assoc m :y
      {:r (.subtract (Ry m) (Rx m))
       :s (.subtract (Sy m) (Sx m))
       :t (.subtract (Ty m) (Tx m))})))

(defn gcd
  "Calculate GCD and weights S and T"
  [a b]
  (let [a (.toBigInteger a)
        b (.toBigInteger b)
        p (remove-powers-of-two a b)]
    (loop [m (assoc p
                :x {:r (:a p)
                    :s java.math.BigInteger/ONE
                    :t java.math.BigInteger/ZERO}
                :y {:r (:b p)
                    :s java.math.BigInteger/ZERO
                    :t java.math.BigInteger/ONE})]
      (cond
        (= (Rx m) (Ry m))
          {:gcd (.shiftLeft (Rx m) (m :c)) :s (Sx m) :t (Tx m)}
        (all-even? [(Ry m)])
          (recur (assoc m :x (:y m) :y (:x m)))
        (all-even? [(Rx m)])
          (recur (Rx-is-even m))
        :else
          (recur (Rx-is-odd m))))))
