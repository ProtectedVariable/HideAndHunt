package ch.epfl.sdp.lobby.global

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.databinding.FragmentGlobalLobbyBinding
import ch.epfl.sdp.db.IRepoFactory
import ch.epfl.sdp.game.data.Game

class GlobalLobbyFragment : Fragment() {

    private var _binding: FragmentGlobalLobbyBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewAdapter: GlobalLobbyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var repo: IGlobalLobbyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repo = (it.getSerializable(REPO_FACTORY_ARG) as IRepoFactory).makeGlobalLobbyRepository()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGlobalLobbyBinding.inflate(inflater)
        repo.getAllGames { games ->
            viewManager = LinearLayoutManager(context)
            viewAdapter = GlobalLobbyAdapter(games)
            binding.globalLobbyRecycler.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }

        binding.globalLobbySwiperefresh.setOnRefreshListener {
            repo.getAllGames { games ->
                viewAdapter.data = games
                viewAdapter.notifyDataSetChanged()
                binding.globalLobbySwiperefresh.isRefreshing = false
            }

        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REPO_FACTORY_ARG = "repoFacto"

        @JvmStatic
        fun newInstance(factory: IRepoFactory) = GlobalLobbyFragment().apply {
            val args = Bundle()
            args.putSerializable(REPO_FACTORY_ARG, factory)
            this.arguments = args
        }
    }
}