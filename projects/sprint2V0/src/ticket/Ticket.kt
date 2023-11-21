package ticket

data class Ticket(val id: Int, val weight: Float, val repr: String, val generationTimeMs: Long = System.currentTimeMillis()) {

}
