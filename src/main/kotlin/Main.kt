import java.io.File

fun main() {

    val rotations = object {}.javaClass
        .getResource("/input")
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

        if(direction == 'L'){
            position -= distance
        } else if(direction == 'R'){
            position += distance
        }

        position = (position % 100 + 100) % 100
        if(position == 0){
            zeroCounter += 1
        }


        println("After $rotation, position is $position")
    }

    println("Zero has been counted " + zeroCounter + " times.")
    println("Password is " + zeroCounter)
}