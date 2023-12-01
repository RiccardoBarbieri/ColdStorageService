package utils

import unibo.landmarks.LandmarkCoordinates

class MapUtils {

    companion object {
        fun loadMapConfiguration(mapName: String): LandmarkCoordinates {
            val landmarkCoordinates = LandmarkCoordinates()
            landmarkCoordinates.load(mapName)
            return landmarkCoordinates
        }
    }
}