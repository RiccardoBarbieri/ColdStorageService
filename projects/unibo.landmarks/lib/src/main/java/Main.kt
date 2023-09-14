

val landmarkCoordinates: unibo.landmarks.LandmarkCoordinates = unibo.landmarks.LandmarkCoordinates()

fun main(args: Array<String>) {
    val filename: String = "servicearea"
    landmarkCoordinates.load(filename)
    println(landmarkCoordinates.getCoordinates())
}