package ch.epfl.sdp.game

import android.content.Intent
import ch.epfl.sdp.game.data.Player

object GameIntentUnpacker {

    data class GameIntentData(val gameID: String, val playerID: Int, val initialTime: Long, val playerList: List<Player>, val mqttURI: String?)

    fun unpack(intent: Intent): Pair<GameIntentData, Boolean> {
        var valid = true
        val gameID = intent.getStringExtra("gameID").orEmpty()
        val playerID = intent.getIntExtra("playerID", -1)
        val initialTime = intent.getLongExtra("initialTime", -1)

        if(gameID.isEmpty() || playerID < 0 || initialTime < 0) {
            valid = false
        }

        val playerList = if(intent.getSerializableExtra("players") == null) emptyList() else (intent.getSerializableExtra("players") as List<*>).filterIsInstance<Player>()
        val mqttURI = intent.getStringExtra("mqttURI")

        return Pair(GameIntentData(gameID, playerID, initialTime, playerList, mqttURI), valid)
    }
}