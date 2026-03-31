package day01

fun main() {

    val rotations = object {}.javaClass
        .getResource("/day01/input.txt")
        ?.readText()
        ?.lines()
        ?.filter { it.isNotBlank() }
        ?: error("File not found")

    var position = 50
    var zeroCounter = 0

    println("Starting position: $position")

    for (rotation in rotations) {
        val direction = rotation[0]
        val distance = rotation.substring(1).toInt()

        repeat(distance){
            if(direction == 'L'){
                position -= 1
            } else if(direction == 'R'){
                position += 1
            }
            position = (position % 100 + 100) % 100
            if(position == 0){
                zeroCounter += 1
            }
        }

        println("After $rotation, position is $position")
    }

    println("Zero has been counted " + zeroCounter + " times.")
    println("Password is " + zeroCounter)
}