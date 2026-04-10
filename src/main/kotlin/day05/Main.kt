package day05

fun main() {
    val input = object {}.javaClass
        .getResource("/day05/input.txt")
        ?.readText()
        ?: error("File not found")

    val sections = input.trim().split("\n\n")

    val rangeLines = sections[0].lines()
    val ingredientLines = sections[1].lines()

    val ranges = mutableListOf<Pair<Int, Int>>()

    for (line in rangeLines) {
        val parts = line.split("-")
        val start = parts[0].toInt()
        val end = parts[1].toInt()
        ranges.add(Pair(start, end))
    }

    var freshCount = 0

    for (line in ingredientLines) {
        val ingredientId = line.toInt()
        var isFresh = false

        for ((start, end) in ranges) {
            if (ingredientId in start..end) {
                isFresh = true
            }
        }

        if (isFresh) {
            freshCount++
        }
    }

    println("Total fresh ingredient IDs: $freshCount")
}