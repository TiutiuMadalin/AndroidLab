package com.example.laboratorandroid.game.game

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
import kotlinx.android.synthetic.main.game_edit_fragment.*
import com.example.laboratorandroid.R
import com.example.laboratorandroid.core.TAG
import com.example.laboratorandroid.game.data.Game

class GameEditFragment : Fragment() {
    companion object {
        const val GAME_ID = "GAME_ID"
    }

    private lateinit var viewModel: GameEditViewModel
    private var gameId: String? = null
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(GAME_ID)) {
                gameId = it.getString(GAME_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.game_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val i = game
            if (i != null) {
                i.title = game_title.text.toString()
                i.releaseDate=game_date.text.toString()
                i.version=game_version.text.toString().toDouble();
                viewModel.saveOrUpdateGame(i)
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(GameEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner) { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        }
        viewModel.fetchingError.observe(viewLifecycleOwner) { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        }
        val id = gameId
        if (id == null) {
            game = Game("", "",0.0,"")
        } else {
            viewModel.getGameById(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update items")
                if (it != null) {
                    game = it
                    game_title.setText(it.title)
                    game_date.setText(it.releaseDate)
                    game_version.setText(it.version.toString())
                }
            })
        }
    }
}
