package day03

fun main() {
    val input = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent()

    val banks = input.lines()
    var total = 0

    for (bank in banks) {
        println("Bank: $bank")

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