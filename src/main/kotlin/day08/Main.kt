package day08

fun main() {

    val lines = object {}.javaClass
        .getResource("/day08/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    val points = mutableListOf<Point>()

    for (line in lines) {
        val parts = line.split(",")
        val x = parts[0].toInt()
        val y = parts[1].toInt()
        val z = parts[2].toInt()
        points.add(Point(x, y, z))
    }

    val pairs = mutableListOf<Edge>()

    // Compare every pair of junction boxes
    for (i in 0 until points.size) {
        for (j in i + 1 until points.size) {
            val p1 = points[i]
            val p2 = points[j]

            val dx = (p1.x - p2.x).toLong()
            val dy = (p1.y - p2.y).toLong()
            val dz = (p1.z - p2.z).toLong()

            val distanceSquared = dx * dx + dy * dy + dz * dz

            pairs.add(Edge(i, j, distanceSquared))
        }
    }

    // Sort from shortest distance to longest
    pairs.sortBy { it.distance }

    val parent = IntArray(points.size) { it }
    val size = IntArray(points.size) { 1 }

    // Connect the 1000 closest pairs
    val limit = minOf(1000, pairs.size)

    for (i in 0 until limit) {
        val edge = pairs[i]
        union(edge.a, edge.b, parent, size)
    }

    // Count final circuit sizes
    val circuitSizes = mutableListOf<Int>()

    for (i in points.indices) {
        if (find(i, parent) == i) {
            circuitSizes.add(size[i])
        }
    }

    circuitSizes.sortDescending()

    val answer =
        circuitSizes[0].toLong() *
                circuitSizes[1].toLong() *
                circuitSizes[2].toLong()

    println("Answer: $answer")
}

data class Point(
    val x: Int,
    val y: Int,
    val z: Int
)

data class Edge(
    val a: Int,
    val b: Int,
    val distance: Long
)

fun find(x: Int, parent: IntArray): Int {
    if (parent[x] != x) {
        parent[x] = find(parent[x], parent)
    }
    return parent[x]
}

fun union(a: Int, b: Int, parent: IntArray, size: IntArray) {
    val rootA = find(a, parent)
    val rootB = find(b, parent)

    if (rootA == rootB) {
        return
    }

    // Attach smaller tree to bigger tree
    if (size[rootA] < size[rootB]) {
        parent[rootA] = rootB
        size[rootB] += size[rootA]
    } else {
        parent[rootB] = rootA
        size[rootA] += size[rootB]
    }
}