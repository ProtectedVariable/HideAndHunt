package ch.epfl.sdp.game.data

import java.io.Serializable

open class Player(val id: Int) : Serializable {
    class WrongPlayerFormat: Exception()

    var lastKnownLocation: Location? = null

    override fun toString(): String {
        return "Player{" +
                "id=" + id +
                ", lastKnownLocation=" + lastKnownLocation +
                '}'
    }

}