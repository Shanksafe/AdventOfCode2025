package day06

fun main() {

    val lines = object {}.javaClass
        .getResource("/day06/input.txt")
        ?.readText()
        ?.lines()
        ?: error("File not found")

    val maxWidth = lines.maxOf { it.length }

    // Make all rows the same width
    val paddedLines = mutableListOf<String>()
    for (line in lines) {
        paddedLines.add(line.padEnd(maxWidth, ' '))
    }

    var total = 0L
    var col = maxWidth - 1

    // Scan problem blocks from right to left
    while (col >= 0) {

        // Skip completely empty columns
        var isEmptyColumn = true
        for (row in paddedLines) {
            if (row[col] != ' ') {
                isEmptyColumn = false
                break
            }
        }

        if (isEmptyColumn) {
            col--
            continue
        }

        // Find this problem block
        val endCol = col

        while (col >= 0) {
            var hasCharacter = false

            for (row in paddedLines) {
                if (row[col] != ' ') {
                    hasCharacter = true
                    break
                }
            }

            if (!hasCharacter) {
                break
            }

            col--
        }

        val startCol = col + 1

        val numbers = mutableListOf<Long>()

        // Build numbers by reading COLUMNS from right to left
        for (currentCol in endCol downTo startCol) {
            var digits = ""

            // only use rows above the last row (last row is operator)
            for (r in 0 until paddedLines.size - 1) {
                val ch = paddedLines[r][currentCol]
                if (ch != ' ') {
                    digits += ch
                }
            }

            if (digits.isNotEmpty()) {
                numbers.add(digits.toLong())
            }
        }

        // Read operator from bottom row
        val operator = paddedLines.last()
            .substring(startCol, endCol + 1)
            .trim()

        var result = 0L

        if (operator == "+") {
            for (num in numbers) {
                result += num
            }
        } else { // "*"
            result = 1L
            for (num in numbers) {
                result *= num
            }
        }

        total += result
    }

    println("Grand total: $total")
}