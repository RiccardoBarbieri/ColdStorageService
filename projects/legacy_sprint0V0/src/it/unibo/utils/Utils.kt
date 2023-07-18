package it.unibo.utils

enum class MState {
    MOVING, STOPPED, HOME
}

enum class SState {
    SERVING, WAITING
}

fun toPair(s: String): Pair<Int, Int> {
    val s1 = s.substring(3).substring(1, s.length - 1)
    val (x, y) = s1.split(",")
    return Pair(x.toInt(), y.toInt())
}