package utils

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
    }


}