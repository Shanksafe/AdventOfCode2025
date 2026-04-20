package day09

fun main() {

    val lines = object {}.javaClass
        .getResource("/day09/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    val redPoints = mutableListOf<Point>()

    for (line in lines) {
        val parts = line.split(",")
        val x = parts[0].toInt()
        val y = parts[1].toInt()
        redPoints.add(Point(x, y))
    }

    val xs = mutableSetOf<Int>()
    val ys = mutableSetOf<Int>()

    // Add important coordinates and neighbours for compression
    for (p in redPoints) {
        xs.add(p.x - 1)
        xs.add(p.x)
        xs.add(p.x + 1)

        ys.add(p.y - 1)
        ys.add(p.y)
        ys.add(p.y + 1)
    }

    val sortedX = xs.toMutableList()
    sortedX.sort()

    val sortedY = ys.toMutableList()
    sortedY.sort()

    val xIndex = mutableMapOf<Int, Int>()
    val yIndex = mutableMapOf<Int, Int>()

    for (i in sortedX.indices) {
        xIndex[sortedX[i]] = i
    }

    for (i in sortedY.indices) {
        yIndex[sortedY[i]] = i
    }

    val width = sortedX.size
    val height = sortedY.size

    val boundary = Array(height) { BooleanArray(width) }

    // Draw loop on compressed grid
    for (i in redPoints.indices) {
        val a = redPoints[i]
        val b = redPoints[(i + 1) % redPoints.size]

        if (a.x == b.x) {
            val x = xIndex[a.x]!!
            val startY = minOf(a.y, b.y)
            val endY = maxOf(a.y, b.y)

            for (y in sortedY) {
                if (y in startY..endY) {
                    boundary[yIndex[y]!!][x] = true
                }
            }
        } else if (a.y == b.y) {
            val y = yIndex[a.y]!!
            val startX = minOf(a.x, b.x)
            val endX = maxOf(a.x, b.x)

            for (x in sortedX) {
                if (x in startX..endX) {
                    boundary[y][xIndex[x]!!] = true
                }
            }
        } else {
            error("Adjacent points must be on the same row or column")
        }
    }

    val outside = Array(height) { BooleanArray(width) }
    val queue = ArrayDeque<Cell>()

    queue.add(Cell(0, 0))
    outside[0][0] = true

    val dx = intArrayOf(1, -1, 0, 0)
    val dy = intArrayOf(0, 0, 1, -1)

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        for (dir in 0..3) {
            val nx = current.x + dx[dir]
            val ny = current.y + dy[dir]

            if (nx !in 0 until width || ny !in 0 until height) {
                continue
            }

            if (outside[ny][nx]) {
                continue
            }

            if (boundary[ny][nx]) {
                continue
            }

            outside[ny][nx] = true
            queue.add(Cell(nx, ny))
        }
    }

    val allowed = Array(height) { BooleanArray(width) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            allowed[y][x] = !outside[y][x]
        }
    }

    val badPrefix = Array(height + 1) { IntArray(width + 1) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val bad = if (allowed[y][x]) 0 else 1

            badPrefix[y + 1][x + 1] =
                badPrefix[y][x + 1] +
                        badPrefix[y + 1][x] -
                        badPrefix[y][x] +
                        bad
        }
    }

    fun countBad(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        val left = minOf(x1, x2)
        val right = maxOf(x1, x2)
        val top = minOf(y1, y2)
        val bottom = maxOf(y1, y2)

        val gx1 = xIndex[left]!!
        val gx2 = xIndex[right]!!
        val gy1 = yIndex[top]!!
        val gy2 = yIndex[bottom]!!

        return badPrefix[gy2 + 1][gx2 + 1] -
                badPrefix[gy1][gx2 + 1] -
                badPrefix[gy2 + 1][gx1] +
                badPrefix[gy1][gx1]
    }

    var bestArea = 0L

    for (i in 0 until redPoints.size) {
        for (j in i + 1 until redPoints.size) {
            val p1 = redPoints[i]
            val p2 = redPoints[j]

            if (countBad(p1.x, p1.y, p2.x, p2.y) == 0) {
                val rectWidth = kotlin.math.abs(p1.x - p2.x) + 1
                val rectHeight = kotlin.math.abs(p1.y - p2.y) + 1
                val area = rectWidth.toLong() * rectHeight.toLong()

                if (area > bestArea) {
                    bestArea = area
                }
            }
        }
    }

    println("Largest area: $bestArea")
}

data class Point(
    val x: Int,
    val y: Int
)

data class Cell(
    val x: Int,
    val y: Int
)