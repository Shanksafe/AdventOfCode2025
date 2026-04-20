# Day 9
## Summary

## Part 1
I checked every pair of red tiles and treated them as opposite corners of a rectangle.
For each pair, I calculated the tile area using `(abs(x1 - x2) + 1) * (abs(y1 - y2) + 1)` 
and kept the largest result.

## Part 2
I first rebuilt the full red/green loop by drawing the segments between consecutive red tiles, 
then used a flood fill to mark which tiles were outside the loop.
After that, I checked every pair of red tiles as opposite corners and
used a prefix sum to keep only rectangles made entirely of allowed red/green tiles.