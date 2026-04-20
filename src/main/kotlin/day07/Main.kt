package day07

fun main() {

    val lines = object {}.javaClass
        .getResource("/day07/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    val width = lines[0].length

    // Find where S starts on the first row
    var startCol = -1
    for (i in lines[0].indices) {
        if (lines[0][i] == 'S') {
            startCol = i
            break
        }
    }

    if (startCol == -1) {
        error("No S found in the top row")
    }

    // active[col] = is there currently a beam in this column?
    var active = BooleanArray(width)
    active[startCol] = true

    var totalSplits = 0L

    // Go through the grid row by row
    for (row in lines.indices) {
        val nextActive = BooleanArray(width)

        for (col in 0 until width) {

            // If there is no beam here, skip it
            if (!active[col]) {
                continue
            }

            val ch = lines[row][col]

            if (ch == '.' || ch == 'S') {
                // Beam continues straight down
                nextActive[col] = true
            } else if (ch == '^') {
                // Beam splits here
                totalSplits++

                if (col - 1 >= 0) {
                    nextActive[col - 1] = true
                }

                if (col + 1 < width) {
                    nextActive[col + 1] = true
                }
            }
        }

        active = nextActive
    }

    println("Total splits: $totalSplits")
}