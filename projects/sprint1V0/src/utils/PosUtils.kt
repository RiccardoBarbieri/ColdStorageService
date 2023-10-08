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
            val regex = Regex("""s(\d+)(?:l(\d+))*""")
            if (regex.matchEntire(Xs) == null || regex.matchEntire(Ys) == null) {
                throw IllegalArgumentException("Invalid dest string: [$Xs], [$Ys]. Mangled format.")
            }

            var match = regex.matchEntire(Xs) ?: throw IllegalArgumentException("Invalid dest string: [$Xs]. Mangled format.")
//            println(match.groups)
//            println(match.groups.toList().slice(1 until match.groups.size))
            val XsList = match.groups.toList().slice(1 until match.groups.size).mapNotNull { it?.value?.toInt() }.toMutableList()
            match = regex.matchEntire(Ys) ?: throw IllegalArgumentException("Invalid dest string: [$Ys]. Mangled format.")
            val YsList = match.groups.toList().slice(1 until match.groups.size).mapNotNull { it?.value?.toInt() }.toMutableList()


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
            val Xs = destinations.joinToString("l") { it.first.toString() }
            val Ys = destinations.joinToString("l") { it.second.toString() }
            return Pair("s$Xs", "s$Ys")
        }
    }


}