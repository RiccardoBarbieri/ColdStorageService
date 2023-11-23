package ticket

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.streams.asSequence

class TicketManager {

    val CHAR_POOL: List<Char> = ('a'..'z').toList()
    val STRING_LENGTH = 2L

    var incrementalId = 0
    val tickets = mutableMapOf<String, Ticket>()

    fun generateTicket(weight: Float): String {
        val currentId = incrementalId++

        var representation: String = "t"
        representation += weight.toString().replace(".", "")
        representation += "n"
        representation += currentId.toString()
        representation += randomString()

        val hash = getHash(representation)
        tickets[hash] = Ticket(currentId, weight, hash, validated = false, validationTimeMs = 0)
        return representation
    }

    fun init(filename: String = "tickets.json", destination: String = "./") {
        val map = loadFromJsonFile(filename, destination)
        map.forEach { (key, value) -> tickets[key] = value }
        incrementalId = tickets.maxOf { it.value.id } + 1
    }

    private fun randomString(): String {
        val sr: SecureRandom = SecureRandom.getInstance("SHA1PRNG")
        return sr.ints(STRING_LENGTH, 0, CHAR_POOL.size)
            .asSequence()
            .map(CHAR_POOL::get)
            .joinToString("")
    }

    private fun getHash(input: String, algorithm: String = "SHA-256"): String {
        val bytes = MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
        return bytes.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun checkTicketValidity(ticketRepr: String, currentTimeMs: Long, ticketTimeMs: Long): Boolean {

        val hash = getHash(ticketRepr)

        val ticket = tickets[hash] ?: return false
        if (ticket.validated) return false

        ticket.validated = true
        ticket.validationTimeMs = currentTimeMs
        tickets[hash] = ticket

        return (currentTimeMs - ticket.generationTimeMs) < ticketTimeMs
    }

    fun dumpToJsonFile(filename: String = "tickets.json", destination: String = "./") {
        val objectMapper = ObjectMapper()
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(java.io.File(destination + filename), tickets)
    }

    private fun loadFromJsonFile(filename: String = "tickets.json", destination: String = "./"): Map<String, Ticket> {
        val objectMapper = ObjectMapper()
        val typeRef: TypeReference<HashMap<String, Ticket>> = object : TypeReference<HashMap<String, Ticket>>() {}
        return objectMapper.readValue(java.io.File(destination + filename), typeRef)
    }


}

fun main() {
    val ticketManager = TicketManager()
    ticketManager.generateTicket(1.0f)
    ticketManager.generateTicket(2.0f)
    ticketManager.generateTicket(3.0f)
    ticketManager.generateTicket(4.0f)
    ticketManager.dumpToJsonFile()
}