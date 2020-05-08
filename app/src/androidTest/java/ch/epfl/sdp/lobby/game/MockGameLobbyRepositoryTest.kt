package ch.epfl.sdp.lobby.game

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.game.PlayerFaction
import ch.epfl.sdp.game.data.Participation
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MockGameLobbyRepositoryTest {
    private val glr = MockGameLobbyRepository


    @Test
    fun getGameNameCallbackIsCalled() {
        var called = false
        glr.getGameName(0) { called = true }
        assertTrue(called)
    }

    @Test
    fun getGameDurationCallbackIsCalled() {
        var called = false
        glr.getGameDuration(0) { called = true }
        assertTrue(called)
    }

    @Test
    fun getParticipationsCallbackIsCalled() {
        var called = false
        glr.getParticipations(0) { called = true }
        assertTrue(called)
    }

    @Test
    fun getPlayersCallbackIsCalled() {
        var called = false
        glr.getPlayers(0) { called = true }
        assertTrue(called)
    }

    @Test
    fun getAdminIdCallbackIsCalled() {
        var called = false
        glr.getAdminId(0) { called = true }
        assertTrue(called)
    }

    @Test
    fun getParticipationsAddsAPlayer() {
        var nPlayers = 0
        glr.getParticipations(0) { players -> nPlayers = players.size }
        var newNPlayers = 0
        glr.getParticipations(0) { players -> newNPlayers = players.size }
        assertEquals(nPlayers + 1, newNPlayers)
    }

    @Test
    fun changePlayerReadyChangesReady() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations(0) {
            players -> playerList = players
        }
        val player1IsReady = playerList[3].ready
        val player2IsReady = playerList[4].ready
        glr.changePlayerReady(0, playerList[3].playerID)
        glr.changePlayerReady(0, playerList[4].playerID)

        glr.getParticipations(0) {
            players -> playerList = players
        }

        val newPlayer1IsReady = playerList[3].ready
        val newPlayer2IsReady = playerList[4].ready


        assertEquals(player1IsReady, !newPlayer1IsReady)
        assertEquals(player2IsReady, !newPlayer2IsReady)
    }

    @Test
    fun setPlayerFactionSetsFaction() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations(0) {
            players -> playerList = players
        }

        glr.setPlayerFaction(0, playerList[1].playerID, PlayerFaction.PREDATOR)
        glr.getParticipations(0) {
            players -> playerList = players
        }
        assertEquals(playerList[1].faction, PlayerFaction.PREDATOR)

        glr.setPlayerFaction(0, playerList[1].playerID, PlayerFaction.PREY)
        glr.getParticipations(0) {
            players -> playerList = players
        }
        assertEquals(playerList[1].faction, PlayerFaction.PREY)
    }

    @Test
    fun setPlayerReadyChangesReadyness() {
        var playerList = Collections.emptyList<Participation>()
        glr.getParticipations(0) {
            players -> playerList = players
        }

        glr.setPlayerReady(0, playerList[3].playerID, false)
        glr.setPlayerReady(0, playerList[4].playerID, true)

        glr.getParticipations(0) {
            players -> playerList = players
        }

        val newPlayer1IsReady = playerList[3].ready
        val newPlayer2IsReady = playerList[4].ready


        assertEquals(false, newPlayer1IsReady)
        assertEquals(true, newPlayer2IsReady)
    }
}