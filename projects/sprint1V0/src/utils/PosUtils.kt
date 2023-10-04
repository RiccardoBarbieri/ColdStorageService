package utils

import kotlin.math.pow
import kotlin.math.sqrt

class PosUtils {

    companion object {

        @Throws(IllegalArgumentException::class)
        fun posStringToPair(pos: String): Pair<Int, Int> {
            //validate
            val regex = Regex("""pos\((\d),(\d)\)""")
            val matchResult = regex.matchEntire(pos) ?: throw IllegalArgumentException("Invalid pos string: $pos")

            val x = matchResult.groupValues[1].toInt()
            val y = matchResult.groupValues[2].toInt()
            return Pair(x, y)
        }

        @Throws(IllegalArgumentException::class)
        fun destStringListToPairs(Xs: String, Ys: String): MutableList<Pair<Int, Int>> {
            val regex = Regex("""\d+(?:,\d+)*""")
            if (regex.matchEntire(Xs) == null || regex.matchEntire(Ys) == null) {
                throw IllegalArgumentException("Invalid dest string: [$Xs], [$Ys]. Mangled format.")
            }

            val XsList = Xs.split(",").map { it.toInt() }
            val YsList = Ys.split(",").map { it.toInt() }

            if (XsList.size != YsList.size) {
                throw IllegalArgumentException("Invalid dest string: [$Xs], [$Ys]. Mismatched size.")
            }

            return XsList.zip(YsList).toMutableList()
        }

        fun closestDestination(position: Pair<Int, Int>, destinations: MutableList<Pair<Int, Int>>): Pair<Int, Int> {
            return destinations.minByOrNull { distance(position, it) } ?: throw IllegalArgumentException("Empty destinations list")
        }

        private fun distance(x1: Pair<Int, Int>, x2: Pair<Int, Int>): Double {
            return sqrt((x1.first - x2.first).toDouble().pow(2.0) + (x1.second - x2.second).toDouble().pow(2.0))
        }

        fun listOfDestToMessStrings(destinations: List<Pair<Int, Int>>): Pair<String, String> {
            val Xs = destinations.joinToString(",") { it.first.toString() }
            val Ys = destinations.joinToString(",") { it.second.toString() }
            return Pair(Xs, Ys)
        }
    }


}