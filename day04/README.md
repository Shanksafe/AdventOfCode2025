# Day 4
## Summary

## Part 1
Im going to be honest this one was really hard to wrap head around 
therefore I heavily relied on AI. Anyway, the problem was to find for every '@' 
if there were less than 4 other '@' around it. This was completed by
treating the input as a 2d matrix and indexing through each row and column. Then using
the values 'dr' and 'dc' to move around the matrices with up, down, left, right and in the diagonals. 
## Part 2



Part 2 changes the problem from a single accessibility check into a repeated 
removal process. Any roll with less than 4 neighboring rolls can be removed and once those rolls are removed
new rolls may become accessible.

The solution scans the grid, collects all removable rolls for 
the current state, removes them as a batch and continues until no more rolls can be removed. 
