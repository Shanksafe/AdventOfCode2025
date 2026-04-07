package day03

fun main() {
    val banks = object {}.javaClass
        .getResource("/day03/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    var total = 0

    for (bank in banks) {
        val digits = bank.toCharArray()
        var best = 0

        for (i in digits.indices) {
            for (j in i + 1 until digits.size) {
                val first = digits[i]
                val second = digits[j]

                val value = "$first$second".toInt()

                if (value > best) {
                    best = value
                }
            }
        }

        println("Best for this bank: $best")
        total += best
    }

    println("Total output joltage: $total")
}