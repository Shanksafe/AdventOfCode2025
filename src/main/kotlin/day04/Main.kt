package day04

fun countNeighbors(grid: List<MutableList<Char>>, r: Int, c: Int): Int {
    var count = 0

    for (dr in -1..1) {
        for (dc in -1..1) {
            if (dr == 0 && dc == 0) continue

            val newRow = r + dr
            val newCol = c + dc

            if (
                newRow in grid.indices &&
                newCol in grid[newRow].indices &&
                grid[newRow][newCol] == '@'
            ) {
                count++
            }
        }
    }

    return count
}

fun main() {
    val grid = object {}.javaClass
        .getResource("/day04/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?.map { it.toMutableList() }
        ?.toMutableList()
        ?: error("File not found")

    var totalRemoved = 0

    while (true) {
        val toRemove = mutableListOf<Pair<Int, Int>>()

        for (r in grid.indices) {
            for (c in grid[r].indices) {
                if (grid[r][c] == '@') {
                    val neighborCount = countNeighbors(grid, r, c)

                    if (neighborCount < 4) {
                        toRemove.add(Pair(r, c))
                    }
                }
            }
        }

        if (toRemove.isEmpty()) {
            break
        }

        for ((r, c) in toRemove) {
            grid[r][c] = '.'
        }

        totalRemoved += toRemove.size
    }

    println("Total removable rolls: $totalRemoved")
}