package utils

import kotlin.math.pow
import kotlin.math.sqrt

class Utils {

    companion object {
        fun distance(coord1: Pair<Int, Int>, coord2: Pair<Int, Int>): Double {
            return sqrt((coord1.first - coord2.first).toDouble().pow(2.0) + (coord1.second - coord2.second).toDouble().pow(2.0))
        }
    }
}