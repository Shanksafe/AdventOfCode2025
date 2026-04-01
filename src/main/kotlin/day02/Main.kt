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
    val input = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"

    val ranges = input.split(",")

    for (range in ranges) {
        val parts = range.split("-")

        val start = parts[0].toLong()
        val end = parts[1].toLong()

        println("Range: $start to $end")

        for (number in start..end) {
            println(number)
        }
    }
}