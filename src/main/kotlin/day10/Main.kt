package day10

fun main() {

    val lines = object {}.javaClass
        .getResource("/day10/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    var totalPresses = 0L

    for (line in lines) {
        val targets = parseTargets(line)
        val buttons = parseButtons(line)

        val patterns = buildPatterns(buttons, targets.size)
        val fewest = findFewestPresses(targets, patterns)

        totalPresses += fewest
    }

    println("Fewest total presses: $totalPresses")
}

fun parseTargets(line: String): IntArray {
    val start = line.indexOf('{')
    val end = line.indexOf('}')

    val inside = line.substring(start + 1, end)
    val parts = inside.split(",")

    val targets = IntArray(parts.size)

    for (i in parts.indices) {
        targets[i] = parts[i].trim().toInt()
    }

    return targets
}

fun parseButtons(line: String): List<IntArray> {
    val buttons = mutableListOf<IntArray>()

    var index = 0

    while (index < line.length) {
        if (line[index] == '(') {
            val end = line.indexOf(')', index)
            val inside = line.substring(index + 1, end)

            if (inside.isBlank()) {
                buttons.add(IntArray(0))
            } else {
                val parts = inside.split(",")
                val button = IntArray(parts.size)

                for (i in parts.indices) {
                    button[i] = parts[i].trim().toInt()
                }

                buttons.add(button)
            }

            index = end + 1
        } else if (line[index] == '{') {
            break
        } else {
            index++
        }
    }

    return buttons
}

data class PatternOption(
    val contribution: IntArray,
    val presses: Int
)

fun buildPatterns(buttons: List<IntArray>, counterCount: Int): Map<Int, List<PatternOption>> {
    val grouped = mutableMapOf<Int, MutableList<PatternOption>>()

    val buttonCount = buttons.size
    val totalSubsets = 1 shl buttonCount

    for (subset in 0 until totalSubsets) {
        val contribution = IntArray(counterCount)
        var parityMask = 0
        var presses = 0

        for (buttonIndex in 0 until buttonCount) {
            if ((subset and (1 shl buttonIndex)) != 0) {
                presses++

                val button = buttons[buttonIndex]

                for (counter in button) {
                    contribution[counter]++
                    parityMask = parityMask xor (1 shl counter)
                }
            }
        }

        grouped.getOrPut(parityMask) { mutableListOf() }
            .add(PatternOption(contribution, presses))
    }

    return grouped
}

fun findFewestPresses(
    targets: IntArray,
    patterns: Map<Int, List<PatternOption>>
): Int {
    val memo = HashMap<String, Int>()
    val INF = 1_000_000_000

    fun solve(currentTarget: IntArray): Int {
        var allZero = true
        for (value in currentTarget) {
            if (value != 0) {
                allZero = false
                break
            }
        }

        if (allZero) {
            return 0
        }

        val key = currentTarget.joinToString(",")

        val cached = memo[key]
        if (cached != null) {
            return cached
        }

        var parityMask = 0
        for (i in currentTarget.indices) {
            if (currentTarget[i] % 2 == 1) {
                parityMask = parityMask or (1 shl i)
            }
        }

        val options = patterns[parityMask]
        if (options == null) {
            memo[key] = INF
            return INF
        }

        var best = INF

        for (option in options) {
            val remaining = IntArray(currentTarget.size)
            var valid = true

            for (i in currentTarget.indices) {
                remaining[i] = currentTarget[i] - option.contribution[i]

                if (remaining[i] < 0 || remaining[i] % 2 != 0) {
                    valid = false
                    break
                }
            }

            if (!valid) {
                continue
            }

            val halfTarget = IntArray(currentTarget.size)
            for (i in currentTarget.indices) {
                halfTarget[i] = remaining[i] / 2
            }

            val subAnswer = solve(halfTarget)
            if (subAnswer == INF) {
                continue
            }

            val candidate = option.presses + 2 * subAnswer

            if (candidate < best) {
                best = candidate
            }
        }

        memo[key] = best
        return best
    }

    val answer = solve(targets)

    if (answer >= INF) {
        error("Target state could not be reached")
    }

    return answer
}