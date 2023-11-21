package ticket

import java.util.concurrent.ThreadLocalRandom
import kotlin.streams.asSequence

class TicketManager {

    val CHAR_POOL: List<Char> = ('a'..'z').toList()
    val STRING_LENGTH = 2L

    var incrementalId = 0
    val tickets = mutableMapOf<Int, Ticket>()

    fun generateTicket(weight: Float): String {
        val currentId = incrementalId++

        var representation: String = "t"
        representation += weight.toString().replace(".", "")
        representation += "n"
        representation += currentId.toString()
        representation += randomString()
        tickets[currentId] = Ticket(currentId, weight, representation)
        return representation
    }

    private fun randomString(): String {
        return ThreadLocalRandom.current().ints(STRING_LENGTH, 0, CHAR_POOL.size)
            .asSequence()
            .map(CHAR_POOL::get)
            .joinToString("")
    }

    fun checkTicketValidity(ticketRepr: String, currentTimeMs: Long, ticketTimeMs: Long): Boolean {
        val ticketId = ticketRepr.split("n")[1].dropLast(2).toInt()
        val ticket = tickets[ticketId] ?: return false
        return (currentTimeMs - ticket.generationTimeMs) < ticketTimeMs
    }

}

fun main() {
    val ticketManager = TicketManager()

}