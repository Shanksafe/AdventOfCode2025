# Day 10
## Summary

## Part 1
I converted each machine into bitmasks: one target mask for the light pattern and 
one mask for each button. Then I used BFS from the all-off state, where each move 
is pressing one button once, so the first time I reached the target gave the fewest presses.

## Part 2
A lot of AI used for this part, BFS and recursion is always a nightmare.
Each button press adds +1 to certain counters. The goal is to reach a target vector with the fewest total presses.

Instead of brute force, we use a parity-based recursive approach:

Match which counters must be odd using a subset of buttons (like Part 1)
Subtract that once → remaining values become even
Divide by 2 and recurse
Use memoization to avoid recomputation

This reduces the problem size each step and is much faster than BFS.