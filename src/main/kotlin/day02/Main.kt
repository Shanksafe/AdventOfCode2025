package day02

fun main() {
    val input = object {}.javaClass
        .getResource("/day02/input.txt")
        ?.readText()
        ?: error("File not found")

    println(input)


}