# Greatest common factor and weights algorithm

The Euclidean GCD extended binary argorithm (https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm) is
a mechanism for finding the lowest common factor of 2 numbers, and also finding two weights that, when multiplied
by the 2 original numbers, produce the common factor.

It works because we pick transformations of the 2 numbers that preserve the lowest common factor.  For instance in
the traditional Euclidean algorithm, we reduce the value by performing:

`Rn = Rn-2 - Q.Rn-1`

where values R are the results of applying the GCD preserving transformation (R0 = a, R1 = b, the initial values). 
Q is any value, but typically the Euclidean quotient (integer part) of Rn-2 / Rn-1.  This is often written as
`Rn-2 (mod Rn-1)`.

This can be seen to be true because if `R0 = a = md`, and `R1 = b = nd`, where `d` is the common factor and `m` and `n`
are co-prime then:

```
R2 = R0 - Q.R1

R2 = md - Q.nd

R2 = d(m - Q.n)
```

Therefore R2 is also a multiple of `d`.  `m` and `n` must be co-prime because if they shared a common factor then
this could be extracted from both and multiplied into the GCF `d`.

By repeating this transformation, we can reduce the values of Rn, until one of them becomes 0, when we know that
the previous one was the GCF `d`.


# Binary algorithm

The quotient Q is expensive for binary machines and instead a binary algorithm can be used.  In the
binary algorithm, we first remove all common `2`s from the prime factors.  For instance if `a = 2^4 . 3^2 . 5`, and
`b = 2^3 . 5 . 7` then we remove `2^3` from both numbers.  Eventually we will multiply the 2s back into the GCD.

Now we have 2 numbers, `a` and `b`, one of which is definitely odd, because one of them will have no powers of 2 in
their prime factorization.  We will call the odd one `a`, and if that isn't the case we will swap the labels so that
it is.

`b` is either even or odd.  If it is even, then we can discard the factor of 2 because `a` does not have it in
common.   If it is odd, then we can apply a transformation to either `a` or `b` that preserves the GCF, but produces
an even number.

`Rn = Rn-1 - Rn-2`

Since both `Rn-1` (a) and `Rn-2` (b) are odd, the result of the difference is even.  We can then remove the factor
of 2 as we did above.

We can see that `Rn = Rn-1 - Rn-2` preserves the GCF.  If `Rn-1 = md` and `Rn-2 = nd`, where `m` and `n` are co-prime
as before, then:

```
Rn = md - nd

Rn = d . (m - n)
```

# The extended algorithm

In additon to finding `d`, the GCF, we would like to find two integers `s` and `t` which satisfy:

`d = s.a + t.b`

where `a` and `b` are the initial values.

We can find these by starting with two sets of `s` and `t` (one for each starting value), and tracking them
through the transformations:

`Rx = a.Sx + b.Tx`
`Ry = a.Sy + b.Ty`

If `Rx = a` and `Ry = b`, then it should be obvious that:

`Rx = a.1 + b.0`.  ie Sx=1, Tx=1
`Ry = a.0 + b.1`.  ie Sx=0, Tx=1

When, in the binary algorithm, we subtract the 2 values, we have:

`Rx -> Rx - Ry`

which means that:

```
a.Sx + b.Tx  ->  a.Sx + b.Tx - a.Sy - b.Ty

a.Sx + b.Tx  ->  a.Sx - a.Sy + b.Tx - b.Ty

a.Sx + b.Tx  ->  a.(Sx - Sy) + b.(Tx - Ty)
```

therefore when `Rx -> Rx - Ry`

```
Sx  -> Sx - Sy
Tx  -> Tx - Ty
```

In the case where we discard the factor of two, it is slightly more complex.

```
Rx -> Rx / 2

a.Sx + b.Tx  ->  (a.Sx + b.Tx) / 2  ->  a.(Sx/2) + b.(Tx/2)

Sx  ->  Sx/2
Tx  ->  Tx/2
```

This only works if Sx and Tx are both even (they must remain integers).

In the case where they are not both even, we can use a different transformation for `Sx` and `Tx`. We
know that Rx is even, and at most one of `a` and `b` are even, because we discarded common powers of 2 from `a` and `b`
before we started these transformations.  So, if `a` is odd then `Sx` is even, and if `b` is odd then
`Tx` is even.  If `a` and `b` are both odd, then so are `Sx` and `Tx`.

```
Acceptable combinations when Sx and Tx are not both even:
Rx(even) = a(odd) . Sx(odd) + b(odd) . Tx(odd)
Rx(even) = a(odd) . Sx(even) + b(even) . Tx(odd)
Rx(even) = a(even) . Sx(odd) + b(odd) . Tx(even)
```

So we use the following transformation when `Sx` and `Tx` are not both even.

```
Sx  ->  (Sx + b)/2
Tx  ->  (Tx - a)/2
```

You can see that `Sx + b` must be even, and `Tx - a` must also be even.

And you can see that the transformation produces the correct division by 2 by substituting the new Sx and Tx
into the Rn transformation:

```
a.Sx + b.Tx  ->  a.(Sx + b)/2 + b.(Tx - a)/2

a.Sx + b.Tx  ->  a.Sx/2 + a.b/2 + b.Tx/2 - b.a/2

a.Sx + b.Tx  ->  a.Sx/2 + b.Tx/2 + (a.b/2 - a.b/2)

a.Sx + b.Tx  ->  a.Sx/2 + b.Tx/2

a.Sx + b.Tx  ->  (a.Sx + b.Tx) / 2

Rn  ->  Rn / 2
```

# Complete algorithm

```
1)  Start with 'a' and 'b'
2)  Remove common factors of 2 from both and remember how many factors of 2 there were (=c)
3)  Make 'Rx' = 'a'
4)  Make `Ry` = 'b'
5)  Set 'Sx' = '1', 'Sy' = '0', 'Tx' = '0', 'Ty' = '1'
6)  Repeat until Rx==Ry:
7)    If 'Rx' is odd:
8)      If (Rx > Ry):
9)        Rx -> Rx - Ry
10)       Sx -> Sx - Sy
11)       Tx -> Tx - Ty
12)     Else:
13)       Ry -> Ry - Rx
14)       Sy -> Sy - Sx
15)       Ty -> Ty - Tx
16)   Else: (Rx is even)
17)     Rx -> Rx / 2
18)     If Sx==even AND Sy==even:
19)       Sx -> Sx / 2
20)       Tx -> Tx / 2
21)     Else:
22)       Sx -> (Sx + b) / 2
23)       Tx -> (Tx - a) / 2
24) (at this point Rx == Ry == GCD without the common powers of 2) 
25) GCD = Rx * 2^c
26) S = Sx
27) T = Tx
```

# Performance

Performance is surprisingly good, in my opinion.

It produces the GCD and weights of two random 1024bit numbers in 2.4 seconds.  Much of this seems to be JVM overhead
as the GCD of 1 and 1 takes 2.28 seconds.

