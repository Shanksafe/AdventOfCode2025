package day05

fun main() {

    val input = object {}.javaClass
        .getResource("/day05/input.txt")
        ?.readText()
        ?: error("File not found")

    val sections = input.trim().split(Regex("""\r?\n\r?\n"""))

    val rangeLines = sections[0].lines().filter { it.isNotBlank() }

    // Step 1: store ranges in a list
    val ranges = mutableListOf<Pair<Long, Long>>()

    for (line in rangeLines) {
        val parts = line.split("-")
        val start = parts[0].toLong()
        val end = parts[1].toLong()
        ranges.add(Pair(start, end))
    }

    // Step 2: sort ranges by start
    ranges.sortBy { it.first }

    // Step 3: merge ranges
    val merged = mutableListOf<Pair<Long, Long>>()

    var currentStart = ranges[0].first
    var currentEnd = ranges[0].second

    for (i in 1 until ranges.size) {
        val nextStart = ranges[i].first
        val nextEnd = ranges[i].second

        if (nextStart <= currentEnd + 1) {
            // overlap -> extend the current range
            if (nextEnd > currentEnd) {
                currentEnd = nextEnd
            }
        } else {
            // no overlap -> save current range
            merged.add(Pair(currentStart, currentEnd))

            // start new range
            currentStart = nextStart
            currentEnd = nextEnd
        }
    }

    // add the last range
    merged.add(Pair(currentStart, currentEnd))

    // Step 4: count total numbers
    var total = 0L

    for (range in merged) {
        val start = range.first
        val end = range.second

        total += (end - start + 1)
    }

    println("Total fresh ingredient IDs: $total")
}