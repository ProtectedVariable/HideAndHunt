package ch.epfl.sdp.replay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityReplayBinding
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import java.io.File

class ReplayActivity : AppCompatActivity() {
    private var replayPath: String? = null
    private lateinit var binding: ActivityReplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Replay: Error"
        replayPath =
                if (savedInstanceState != null) savedInstanceState.getString(REPLAY_PATH_ARG)
                else intent.getStringExtra(REPLAY_PATH_ARG)

        getReplayFile()?.let {file ->
            val gameHistory = GameHistory.fromFile(file.inputStream())
            title = "Replay: Game #${gameHistory.gameID} by ${gameHistory.adminID}"

            AndroidGraphicFactory.createInstance(application)

            val (firstTimestamp, lastTimestamp) = gameHistory.events.map { it.timestamp }.let { Pair(it.min()!!, it.max()!!) }
            if (savedInstanceState == null) {
                setupFragments(firstTimestamp, lastTimestamp, gameHistory)
            }
        }
    }

    private fun setupFragments(firstTimestamp: Int, lastTimestamp: Int, gameHistory: GameHistory) {
        supportFragmentManager.beginTransaction()
                .replace(binding.replayControl.id, ReplayControlFragment.newInstance(firstTimestamp, lastTimestamp))
                .replace(binding.replayMap.id, ReplayFragment.newInstance(gameHistory))
                .commitNow()
    }

    private fun getReplayFile() : File? {
        if (replayPath == null) {
            binding.errorDetails.text = getString(R.string.missing_replay_path_parameter)
            return null
        }

        val file = File(filesDir.absolutePath + "/replays/" + replayPath!!)
        if (!file.exists() || !file.isFile) {
            binding.errorDetails.text = getString(R.string.file_not_found).format(file.absoluteFile)
            return null
        }

        return file
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(REPLAY_PATH_ARG, replayPath)
    }

    companion object {
        const val REPLAY_PATH_ARG = "replay_path"
    }
}
