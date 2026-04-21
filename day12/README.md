# Day 12
Hello, thank you for looking at my repo !!

2D packing problem: place rotated/flipped shapes (`#`) into regions without overlap.  
Goal: count how many regions can fit all required shapes.

### Approach
- Represent shapes as `(x, y)` coordinates
- Generate all unique rotations + flips
- Expand region counts → individual pieces
- Sort pieces largest-first
- Use backtracking (DFS) to place pieces

### Optimisations
- Skip if required area > region area
- Deduplicate orientations

### Result
Returns number of regions that can fit all shapes.