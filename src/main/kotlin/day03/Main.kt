package day03

fun main() {
    val banks = object {}.javaClass
        .getResource("/day03/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    var total = 0L

    for (bank in banks) {
        val digitsToKeep = 12
        require(bank.length >= digitsToKeep) { "Bank is too short: $bank" }

        var digitsToDrop = bank.length - digitsToKeep
        val result = mutableListOf<Char>()

        for (digit in bank) {
            while (
                result.isNotEmpty() &&
                digitsToDrop > 0 &&
                result.last() < digit
            ) {
                result.removeAt(result.lastIndex)
                digitsToDrop--
            }

            result.add(digit)
        }

        while (digitsToDrop > 0) {
            result.removeAt(result.lastIndex)
            digitsToDrop--
        }

        val best = result.joinToString("")
        total += best.toLong()
    }

    println("Total output joltage: $total")
}