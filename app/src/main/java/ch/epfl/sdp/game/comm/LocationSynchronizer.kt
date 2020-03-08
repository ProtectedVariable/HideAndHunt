package ch.epfl.sdp.game.comm

import ch.epfl.sdp.game.data.Location

interface LocationSynchronizer {
    interface PlayerUpdateListener {
        fun onPlayerLocationUpdate(playerID: Int, location: Location)
        fun onPreyCatches(predatorID: Int, preyID: Int)
    }

    fun updateOwnLocation(location: Location)
    fun declareCatch(playerID: Int)

    fun subscribeToPlayer(playerID: Int)
    fun unsubscribeFromPlayer(playerID: Int)

    fun setPlayerUpdateListener(listener: PlayerUpdateListener)

    fun stop()
}