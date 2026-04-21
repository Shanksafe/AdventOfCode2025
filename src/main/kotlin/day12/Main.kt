package day12

// One occupied square of a shape
data class Cell(
    val x: Int,
    val y: Int
)

// A present shape from the input
// We only store the occupied '#' cells
data class Shape(
    val id: Int,
    val cells: List<Cell>
)

// One region under a tree
// width x height and how many of each shape it needs
data class Region(
    val width: Int,
    val height: Int,
    val counts: List<Int>
)

// One rotated/flipped version of a shape
// width and height are the size of its bounding box
data class Orientation(
    val cells: List<Cell>,
    val width: Int,
    val height: Int
)

// One actual piece we need to place in a region
// shapeId tells us which shape it came from
// orientations is every unique rotated/flipped version
// area is number of occupied cells, used for sorting
data class Piece(
    val shapeId: Int,
    val orientations: List<Orientation>,
    val area: Int
)

fun main() {

    // Read the input file
    val lines = object {}.javaClass
        .getResource("/day12/input.txt")
        ?.readText()
        ?.lines()
        ?: error("File not found")

    // Parse the input into:
    // 1. all shape definitions
    // 2. all regions to test
    val (shapes, regions) = parseInput(lines)

    // Easy lookup by shape id
    val shapeMap = shapes.associateBy { it.id }

    // Precompute all unique orientations for each shape
    val orientationMap = shapes.associate { shape ->
        shape.id to generateOrientations(shape)
    }

    var answer = 0

    // Test each region
    for (region in regions) {
        if (canFitRegion(region, orientationMap, shapeMap)) {
            answer++
        }
    }

    println("Regions that fit: $answer")
}

fun parseInput(lines: List<String>): Pair<List<Shape>, List<Region>> {
    val shapes = mutableListOf<Shape>()
    val regions = mutableListOf<Region>()

    var index = 0

    while (index < lines.size) {
        val line = lines[index].trim()

        if (line.isBlank()) {
            index++
            continue
        }

        // Shape header like: 0:
        if (line.endsWith(":") && !line.contains("x")) {
            val shapeId = line.removeSuffix(":").toInt()
            index++

            val shapeRows = mutableListOf<String>()

            while (index < lines.size) {
                val row = lines[index].trim()

                if (row.isBlank()) {
                    break
                }

                // Stop if we hit another shape header
                if (row.endsWith(":") && !row.contains("x")) {
                    break
                }

                // Stop if we hit a region line
                if (row.contains("x") && row.contains(":") && !row.endsWith(":")) {
                    break
                }

                shapeRows.add(row)
                index++
            }

            val cells = mutableListOf<Cell>()

            for (y in shapeRows.indices) {
                for (x in shapeRows[y].indices) {
                    if (shapeRows[y][x] == '#') {
                        cells.add(Cell(x, y))
                    }
                }
            }

            shapes.add(Shape(shapeId, cells))
            continue
        }

        // Region line like: 12x5: 1 0 1 0 2 2
        if (line.contains("x") && line.contains(":") && !line.endsWith(":")) {
            val parts = line.split(":")
            val sizePart = parts[0].trim()
            val countPart = parts[1].trim()

            val sizePieces = sizePart.split("x")
            val width = sizePieces[0].toInt()
            val height = sizePieces[1].toInt()

            val counts = countPart.split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }

            regions.add(Region(width, height, counts))
        }

        index++
    }

    return Pair(shapes, regions)
}

fun normalize(cells: List<Cell>): List<Cell> {
    // Find the smallest x and y values
    val minX = cells.minOf { it.x }
    val minY = cells.minOf { it.y }

    // Shift shape so its top-left occupied cell starts at (0,0)
    // Then sort cells so equivalent shapes compare the same
    return cells.map { cell ->
        Cell(cell.x - minX, cell.y - minY)
    }.sortedWith(compareBy<Cell> { it.y }.thenBy { it.x })
}

fun rotate90(cells: List<Cell>): List<Cell> {
    // Rotate every cell 90 degrees:
    // (x, y) -> (-y, x)
    return cells.map { cell ->
        Cell(-cell.y, cell.x)
    }
}

fun flipHorizontal(cells: List<Cell>): List<Cell> {
    // Flip every cell across a vertical axis:
    // (x, y) -> (-x, y)
    return cells.map { cell ->
        Cell(-cell.x, cell.y)
    }
}

fun addOrientation(
    cells: List<Cell>,
    unique: MutableMap<String, Orientation>
) {
    // Compute bounding box size
    val maxX = cells.maxOf { it.x }
    val maxY = cells.maxOf { it.y }

    val width = maxX + 1
    val height = maxY + 1

    // Build a string key so duplicate orientations collapse
    val key = cells.joinToString(";") { "${it.x},${it.y}" }

    unique[key] = Orientation(cells, width, height)
}

fun generateOrientations(shape: Shape): List<Orientation> {
    val unique = mutableMapOf<String, Orientation>()

    // Start from original cells
    var rotated = shape.cells

    // Generate 4 rotations
    repeat(4) {
        // Add this rotated version
        val normalizedRotation = normalize(rotated)
        addOrientation(normalizedRotation, unique)

        // Add flipped version of this rotation
        val flipped = flipHorizontal(rotated)
        val normalizedFlipped = normalize(flipped)
        addOrientation(normalizedFlipped, unique)

        // Rotate again for next loop
        rotated = rotate90(rotated)
    }

    return unique.values.toList()
}

fun hasEnoughSpace(region: Region, shapeMap: Map<Int, Shape>): Boolean {
    // Count how many occupied cells all required pieces need
    val requiredArea = region.counts.indices.sumOf { shapeId ->
        region.counts[shapeId] * shapeMap[shapeId]!!.cells.size
    }

    // Total number of cells in the region
    val regionArea = region.width * region.height

    // If required occupied area is bigger than region area, impossible
    return requiredArea <= regionArea
}

fun buildPiecesForRegion(
    region: Region,
    orientationMap: Map<Int, List<Orientation>>,
    shapeMap: Map<Int, Shape>
): List<Piece> {
    val pieces = mutableListOf<Piece>()

    // Expand counts into actual piece objects
    for (shapeId in region.counts.indices) {
        val count = region.counts[shapeId]

        repeat(count) {
            val shape = shapeMap[shapeId]!!
            val orientations = orientationMap[shapeId]!!

            pieces.add(
                Piece(
                    shapeId = shapeId,
                    orientations = orientations,
                    area = shape.cells.size
                )
            )
        }
    }

    // Place bigger pieces first to reduce backtracking
    return pieces.sortedByDescending { it.area }
}

fun canPlace(
    board: Array<BooleanArray>,
    boardWidth: Int,
    boardHeight: Int,
    orientation: Orientation,
    startX: Int,
    startY: Int
): Boolean {
    // Check every occupied cell of the piece
    for (cell in orientation.cells) {
        val x = startX + cell.x
        val y = startY + cell.y

        // Must stay inside the board
        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            return false
        }

        // Cannot overlap another occupied cell
        if (board[y][x]) {
            return false
        }
    }

    return true
}

fun place(
    board: Array<BooleanArray>,
    orientation: Orientation,
    startX: Int,
    startY: Int,
    value: Boolean
) {
    // Mark or unmark the occupied cells of a piece
    for (cell in orientation.cells) {
        val x = startX + cell.x
        val y = startY + cell.y
        board[y][x] = value
    }
}

fun search(
    pieceIndex: Int,
    pieces: List<Piece>,
    board: Array<BooleanArray>,
    boardWidth: Int,
    boardHeight: Int
): Boolean {
    // Base case: all pieces placed successfully
    if (pieceIndex == pieces.size) {
        return true
    }

    val piece = pieces[pieceIndex]

    // Try every orientation of this piece
    for (orientation in piece.orientations) {

        // Try every board position
        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {

                // If this piece fits here
                if (canPlace(board, boardWidth, boardHeight, orientation, x, y)) {

                    // Place it
                    place(board, orientation, x, y, true)

                    // Recurse to place next piece
                    if (search(pieceIndex + 1, pieces, board, boardWidth, boardHeight)) {
                        return true
                    }

                    // Undo if it did not lead to a solution
                    place(board, orientation, x, y, false)
                }
            }
        }
    }

    // No valid placement found for this piece
    return false
}

fun canFitRegion(
    region: Region,
    orientationMap: Map<Int, List<Orientation>>,
    shapeMap: Map<Int, Shape>
): Boolean {
    // Quick impossible check
    if (!hasEnoughSpace(region, shapeMap)) {
        return false
    }

    // Build list of actual pieces that need placing
    val pieces = buildPiecesForRegion(region, orientationMap, shapeMap)

    // Empty board:
    // false = free
    // true = occupied by some piece's '#'
    val board = Array(region.height) { BooleanArray(region.width) }

    // Try to place everything
    return search(0, pieces, board, region.width, region.height)
}