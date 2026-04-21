# Day 10
## Summary

## Part 1
I converted each machine into bitmasks: one target mask for the light pattern and 
one mask for each button. Then I used BFS from the all-off state, where each move 
is pressing one button once, so the first time I reached the target gave the fewest presses.

## Part 2
