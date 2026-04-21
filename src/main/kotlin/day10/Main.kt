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
        val targetMask = parseTargetMask(line)
        val buttonMasks = parseButtonMasks(line)

        val fewest = findFewestPresses(targetMask, buttonMasks)
        totalPresses += fewest
    }

    println("Fewest total presses: $totalPresses")
}

fun parseTargetMask(line: String): Int {
    val start = line.indexOf('[')
    val end = line.indexOf(']')

    //Inside .##.
    val pattern = line.substring(start + 1, end)

    // Storing bits
    var mask = 0

    //shift left shl
    for (i in pattern.indices) {
        if (pattern[i] == '#') {
            mask = mask or (1 shl i)
        }
    }

    return mask
}

fun parseButtonMasks(line: String): List<Int> {
    val buttonMasks = mutableListOf<Int>()

    var index = 0
    //finding buttons
    while (index < line.length) {
        if (line[index] == '(') {
            val end = line.indexOf(')', index)
            val inside = line.substring(index + 1, end)

            var mask = 0

            if (inside.isNotBlank()) {
                val parts = inside.split(",")

                for (part in parts) {
                    val lightIndex = part.trim().toInt()
                    mask = mask or (1 shl lightIndex)
                }
            }

            buttonMasks.add(mask)
            index = end + 1
        } else if (line[index] == '{') {
            // Stop when joltage section starts
            break
        } else {
            index++
        }
    }

    return buttonMasks
}

fun findFewestPresses(targetMask: Int, buttonMasks: List<Int>): Int {
    if (targetMask == 0) {
        return 0
    }

    val queue = ArrayDeque<Int>()
    val distance = HashMap<Int, Int>()

    queue.add(0)
    distance[0] = 0

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentSteps = distance[current]!!

        for (button in buttonMasks) {
            val next = current xor button

            if (!distance.containsKey(next)) {
                distance[next] = currentSteps + 1

                if (next == targetMask) {
                    return currentSteps + 1
                }

                queue.add(next)
            }
        }
    }

    error("Target state could not be reached")
}