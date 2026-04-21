# Day 11
## Summary
What a callback to uni
## Part 1
I treated the devices as a directed graph and used DFS 
to count all paths from `you` to `out`. If the search 
reaches `out`, it returns 1; otherwise it adds together the number of paths from each outgoing connection.

## Part 2
I used DFS with memoization on `(current node, seen dac, seen fft)`.
This avoids recalculating the same subproblems and makes counting valid 
paths from `svr` to `out` much faster.
