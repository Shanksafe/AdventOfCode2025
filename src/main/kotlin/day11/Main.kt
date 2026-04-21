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

        // Make sure every mentioned node exists in the map
        for (next in outputs) {
            graph.getOrPut(next) { mutableListOf() }
        }
    }

    val totalPaths = countPaths("you", graph)

    println("Total paths: $totalPaths")
}

fun countPaths(
    current: String,
    graph: Map<String, List<String>>
): Long {
    if (current == "out") {
        return 1L
    }

    var total = 0L

    val neighbours = graph[current] ?: emptyList()

    for (next in neighbours) {
        total += countPaths(next, graph)
    }

    return total
}