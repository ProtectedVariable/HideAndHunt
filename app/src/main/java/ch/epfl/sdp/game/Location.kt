package ch.epfl.sdp.game

import kotlin.math.*

class Location(var latitude: Double, var longitude: Double) {
    fun distanceTo(other: Location): Double {
        return abs(sqrt(
                (latitude - other.latitude).pow(2.0) + (longitude - other.longitude).pow(2.0)
        ))
    }
}