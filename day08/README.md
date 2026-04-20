# Day 8
## Summary
Hi not going to lie again, HEAVY AI reliance on this one to 
wrap my head around math...

## Part 1
For this problem we read all junction box coordinates then 
calculate the distance between every pair and sorted the 
pairs from shortest to longest. Then connect the first 
1000 pairs using union-find, then find the sizes of all circuits 
and multiplied them 3 largest sizes.

## Part 2
For part 2, I kept connecting the shortest pairs using union-find
until all junction boxes were in one circuit. When the final merge happened, 
I multiplied the X coordinates of thSat last pair to get the answer.