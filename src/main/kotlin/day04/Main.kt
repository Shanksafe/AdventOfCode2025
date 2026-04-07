package day04

fun main() {
    val grid = object {}.javaClass
        .getResource("/day04/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    var accessibleCount = 0

    for (r in grid.indices) {
        for (c in grid[r].indices) {
            if (grid[r][c] == '@') {
                var neighborCount = 0

                for (dr in -1..1) {
                    for (dc in -1..1) {
                        if (dr == 0 && dc == 0) {
                            continue
                        }

                        val newRow = r + dr
                        val newCol = c + dc

                        if (
                            newRow in grid.indices &&
                            newCol in grid[newRow].indices &&
                            grid[newRow][newCol] == '@'
                        ) {
                            neighborCount++
                        }
                    }
                }

                if (neighborCount < 4) {
                    accessibleCount++
                }
            }
        }
    }

    println("Total accessible rolls: $accessibleCount")
}