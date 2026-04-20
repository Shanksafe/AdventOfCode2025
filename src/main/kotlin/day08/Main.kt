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

    // Build every possible pair of points
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

    var circuits = points.size
    var answer = 0L

    for (edge in pairs) {
        val rootA = find(edge.a, parent)
        val rootB = find(edge.b, parent)

        // Only connect if they are in different circuits
        if (rootA != rootB) {
            union(edge.a, edge.b, parent, size)
            circuits--

            // If everything is now one circuit, this was the last needed connection
            if (circuits == 1) {
                answer = points[edge.a].x.toLong() * points[edge.b].x.toLong()
                break
            }
        }
    }

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