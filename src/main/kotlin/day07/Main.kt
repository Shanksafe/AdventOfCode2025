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

    // timelines[col] = how many timelines are currently at this column
    var timelines = LongArray(width)
    timelines[startCol] = 1L

    // Go through the grid row by row
    for (row in lines.indices) {
        val nextTimelines = LongArray(width)

        for (col in 0 until width) {

            val currentCount = timelines[col]

            // If there are no timelines here, skip it
            if (currentCount == 0L) {
                continue
            }

            val ch = lines[row][col]

            if (ch == '.' || ch == 'S') {
                // All timelines keep going straight down
                nextTimelines[col] += currentCount
            } else if (ch == '^') {
                // Timelines split left and right
                if (col - 1 >= 0) {
                    nextTimelines[col - 1] += currentCount
                }

                if (col + 1 < width) {
                    nextTimelines[col + 1] += currentCount
                }
            }
        }

        timelines = nextTimelines
    }

    var total = 0L
    for (count in timelines) {
        total += count
    }

    println("Total timelines: $total")
}