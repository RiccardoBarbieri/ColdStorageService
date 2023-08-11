package utils

fun computeMoves(start: Pair<Int, Int>, end: Pair<Int, Int>): ArrayList<String> {
    val direction = Pair(end.first - start.first, end.second - start.second)
    val moves = ArrayList<String>()
    var i = 0
    if (direction.first == 0) {

    }
    else if (direction.first < 0) {
        moves.add("l")
        moves.add("l")
        while (i < -direction.first) {
            moves.add("s")
            i++
        }
        moves.add("l")
        moves.add("l")
    }
    else {
        while (i < direction.first) {
            moves.add("s")
            i++
        }
    }
    i = 0
    if (direction.second == 0) {

    }
    else if (direction.second < 0) {
        moves.add("r")
        while (i < -direction.second) {
            moves.add("s")
            i++
        }
        moves.add("l")
    }
    else {
        moves.add("l")
        while (i < direction.second) {
            moves.add("s")
            i++
        }
        moves.add("r")
    }
    return moves
}

fun getTempPos(curPos: Pair<Int, Int>, numTurns: Int): Pair<Int, Int> {
    var tempPos: Pair<Int, Int> = Pair(0, 0)

    when (numTurns) {
        0 -> tempPos = Pair(curPos.first, curPos.second - 1)
        1 -> tempPos = Pair(curPos.first -1, curPos.second)
        2 -> tempPos = Pair(curPos.first, curPos.second + 1)
        3 -> tempPos = Pair(curPos.first + 1, curPos.second)
    }

    return tempPos
}

fun getAdjacent(curPost: Pair<Int, Int>): ArrayList<Pair<Int, Int>> {
    val adjacent: ArrayList<Pair<Int, Int>> = ArrayList()
    adjacent.add(Pair(curPost.first, curPost.second - 1))
    adjacent.add(Pair(curPost.first - 1, curPost.second))
    adjacent.add(Pair(curPost.first, curPost.second + 1))
    adjacent.add(Pair(curPost.first + 1, curPost.second))
    return adjacent
}

fun updatePosQueue(curPos: Pair<Int, Int>, obstacles: ArrayList<Pair<Int,Int>>, visited: MutableSet<Pair<Int,Int>>): Collection<Pair<Int, Int>> {
    val pairs: ArrayList<Pair<Int, Int>> = ArrayList()

    val adjacent: ArrayList<Pair<Int, Int>> = getAdjacent(curPos)

    for (adj in adjacent) {
        if (!obstacles.contains(adj) && !visited.contains(adj) && adj.first in 0..4 && adj.second in 0..6) {
            pairs.add(adj)
        }
    }
    return pairs
}