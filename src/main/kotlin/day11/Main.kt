package day11

fun main() {

    val lines = object {}.javaClass
        .getResource("/day11/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    val graph = mutableMapOf<String, MutableList<String>>()

    for (line in lines) {
        val parts = line.split(":")
        val device = parts[0].trim()

        val outputs = if (parts.size > 1 && parts[1].trim().isNotEmpty()) {
            parts[1].trim().split(" ")
        } else {
            emptyList()
        }

        graph.getOrPut(device) { mutableListOf() }.addAll(outputs)

        for (next in outputs) {
            graph.getOrPut(next) { mutableListOf() }
        }
    }

    val memo = HashMap<String, Long>()
    val totalPaths = countPaths("svr", false, false, graph, memo)

    println("Total valid paths: $totalPaths")
}

fun countPaths(
    current: String,
    seenDac: Boolean,
    seenFft: Boolean,
    graph: Map<String, List<String>>,
    memo: HashMap<String, Long>
): Long {

    val newSeenDac = seenDac || current == "dac"
    val newSeenFft = seenFft || current == "fft"

    val key = "$current|$newSeenDac|$newSeenFft"
    val cached = memo[key]
    if (cached != null) {
        return cached
    }

    if (current == "out") {
        val answer = if (newSeenDac && newSeenFft) 1L else 0L
        memo[key] = answer
        return answer
    }

    var total = 0L
    val neighbours = graph[current] ?: emptyList()

    for (next in neighbours) {
        total += countPaths(next, newSeenDac, newSeenFft, graph, memo)
    }

    memo[key] = total
    return total
}