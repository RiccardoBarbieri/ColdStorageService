package unibo.landmarks

import utils.Utils
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


class LandmarkCoordinates(coordinates: MutableMap<String, MutableList<Pair<Int, Int>>>): Serializable {

    private val serialVersionUID = 5L;

    private val coordinates: MutableMap<String, MutableList<Pair<Int, Int>>>

    init {
        this.coordinates = coordinates
    }

    constructor() : this(mutableMapOf())

    fun getCoordinates(): Map<String, List<Pair<Int, Int>>> {
        return coordinates
    }

    @Throws(IllegalArgumentException::class)
    fun getCoordinateClosestToFor(landmark: String, coordinate: Pair<Int, Int>): Pair<Int, Int> {
        val coordinates: List<Pair<Int, Int>> = this.coordinates[landmark] ?: throw IllegalArgumentException("Landmark $landmark not found")

        if (coordinates.isEmpty()) {
            throw IllegalArgumentException("Landmark $landmark has no coordinates")
        }

        if (coordinates.size == 1) {
            return coordinates[0]
        }

        return coordinates.minByOrNull { Utils.distance(it, coordinate) } ?: throw IllegalArgumentException("Landmark $landmark has no coordinates")
    }

    @Throws(IllegalArgumentException::class)
    fun getCoordinatesFor(landmark: String): List<Pair<Int, Int>> {
        return coordinates[landmark] ?: throw IllegalArgumentException("Landmark $landmark not found")
    }

    fun setCoordinates(landmark: String, coordinates: MutableList<Pair<Int, Int>>) {
        this.coordinates[landmark] = coordinates
    }

    fun setCoordinate(landmark: String, coordinate: Pair<Int, Int>) {
        this.coordinates[landmark] = mutableListOf(coordinate)
    }

    fun addCoordinate(landmark: String, coordinate: Pair<Int, Int>) {
        val coordinates: MutableList<Pair<Int, Int>> = this.coordinates[landmark]?.toMutableList() ?: mutableListOf()
        coordinates.add(coordinate)
        this.coordinates[landmark] = coordinates
    }

    @Throws(IllegalArgumentException::class)
    fun dump(filename: String) {
        if (filename.isEmpty()) {
            throw IllegalArgumentException("Filename cannot be empty")
        }
        ObjectOutputStream(FileOutputStream("${filename}Coordinates.bin")).use { it.writeObject(this) }
    }

    fun load(filename: String) {
        ObjectInputStream(FileInputStream("${filename}Coordinates.bin")).use {
            val landmarkCoordinates: LandmarkCoordinates = it.readObject() as LandmarkCoordinates
            this.coordinates.clear()
            this.coordinates.putAll(landmarkCoordinates.coordinates)
        }
    }

}