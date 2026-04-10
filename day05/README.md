# Day 5
## Summary

## Part 1
This problem involves ranges of numbers and finding whether
a value fits within a range. I had a problem where program crashed due to
input file having numbers larger than 'int' could handle so I had to 
use 'long' also used AI to add some code safety.

## Part 2

This solution merges overlapping and adjacent ranges into continuous intervals, 
then counts how many numbers are covered in total.

Ranges are sorted, merged step-by-step, 
and the final count is calculated using `end - start + 1`.
