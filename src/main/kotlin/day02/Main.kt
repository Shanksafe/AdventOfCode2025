package day02

fun isInvalid(number: Long): Boolean {
    val text = number.toString()

    if (text.length % 2 != 0) {
        return false
    }

    val halfLength = text.length / 2
    val firstHalf = text.substring(0, halfLength)
    val secondHalf = text.substring(halfLength)

    return firstHalf == secondHalf
}

fun main() {

    val input = object {}.javaClass
        .getResource("/day02/input.txt")
        ?.readText()
        ?: error("File not found")

    val ranges = input.split(",")

    var total = 0L

    for (range in ranges) {
        val parts = range.split("-")

        val start = parts[0].toLong()
        val end = parts[1].toLong()

        println("Range: $start to $end")

        for (number in start..end) {
            if (isInvalid(number)) {
                println("Invalid ID: $number")
                total += number
            }
        }
    }

    println("Total: $total")
}