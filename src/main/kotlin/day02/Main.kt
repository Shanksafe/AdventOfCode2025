package day02

fun isInvalid(number: Long): Boolean {
    val text = number.toString()

    for (blockLength in 1 until text.length) {
        if (text.length % blockLength != 0) {
            continue
        }

        val repeats = text.length / blockLength
        if (repeats < 2) {
            continue
        }

        val pattern = text.substring(0, blockLength)
        val rebuilt = pattern.repeat(repeats)

        if (rebuilt == text) {
            return true
        }
    }

    return false
}

fun main() {
    val input = object {}.javaClass
        .getResource("/day02/input.txt")
        ?.readText()
        ?.trim()
        ?: error("File not found")

    val ranges = input.split(",")
    var total = 0L

    for (range in ranges) {
        val parts = range.split("-")
        val start = parts[0].toLong()
        val end = parts[1].toLong()

        for (number in start..end) {
            if (isInvalid(number)) {
                println("Invalid ID: $number")
                total += number
            }
        }
    }

    println("Total: $total")
}