# Gray Codes

## Approach

Let `gc(n, i)` denote the `i`-th Gray code of length `n`, where `i` is 1-based.

Gray codes can be built recursively. The 1-bit Gray codes are:

```text
[0, 1]
```

Now suppose we already know all Gray codes of length `n` and want to construct the Gray codes of
length `n + 1`.

For the first half of the `(n + 1)`-bit codes, we prepend `0` to each `n`-bit code in the original
order:

```text
0 gc(n, 1)
0 gc(n, 2)
...
0 gc(n, 2^n)
```

For the second half, we prepend `1`. To preserve the Gray-code property, we use the `n`-bit codes
in reverse order:

```text
0 gc(n, 1)
0 gc(n, 2)
...
0 gc(n, 2^n)
1 gc(n, 2^n)
...
1 gc(n, 1)
```

This construction ensures that every pair of consecutive codes differs by exactly one bit.

## Effective Implementation

To construct a Gray code directly from its 0-based index `i`, observe the following pattern.

For the `n`-th bit, we first get a run of `2^n` codes where the bit goes from `0` to `1`,
and then a run of `2^n` codes where the bit goes from `1` back to `0`.

In other words:

* If bit `n + 1` of the index is `0`, then bit `n` follows the normal `0..1` pattern
* If bit `n + 1` of the index is `1`, then bit `n` follows the **inverted** `1..0` pattern

Here is an example. The binary index is on the left, and the corresponding Gray code is
on the right:

```text
000 - 000
001 - 001
010 - 011
011 - 010
100 - 110
101 - 111
110 - 101
111 - 100
```

Since `i >>> 1` shifts the index right by one bit, each bit of `i >>> 1` tells us whether the
corresponding bit run should be flipped. XOR-ing `i` with `i >>> 1` applies exactly that
transformation.

So the efficient Gray-code function is:

```java
long getGrayCode(long i) {
  return i ^ (i >>> 1);
}
```
