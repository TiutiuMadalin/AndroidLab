package com.example.laboratorandroid.game.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.laboratorandroid.R
import com.example.laboratorandroid.auth.data.AuthRepository
import com.example.laboratorandroid.core.TAG
import kotlinx.android.synthetic.main.game_list_fragment.*

class GameListFragment : Fragment() {
    private lateinit var gameListAdapter: GameListAdapter
    private lateinit var gamesModel: GameListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.fragment_login)
            return;
        }
        setupGameList()
        fab.setOnClickListener {
            Log.v(TAG, "add new game")
            findNavController().navigate(R.id.GameEditFragment)
        }
    }

    private fun setupGameList() {
        gameListAdapter = GameListAdapter(this)
        item_list.adapter = gameListAdapter
        gamesModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        gamesModel.games.observe(viewLifecycleOwner) { games->
            Log.v(TAG, "update games")
            gameListAdapter.games = games
        }
        gamesModel.loading.observe(viewLifecycleOwner) { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        }
        gamesModel.loadingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
        gamesModel.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}