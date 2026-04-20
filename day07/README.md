# Day 7
## Summary
I doubt anyone is reading this but hello

## Part 1
I solved this by scanning the manifold row by row and tracking which columns currently 
contain a beam.
If a beam hits `^`, I count one split and send 
new beams to the left and right columns.

## Part 2
For part 2, I changed the solution to store how many timelines are 
in each column instead of just whether a beam exists.
Each splitter sends all current timelines to both the left and right
and the final answer is the total number of timelines after the last row.
