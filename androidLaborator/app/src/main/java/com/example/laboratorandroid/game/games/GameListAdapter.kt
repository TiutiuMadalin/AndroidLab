package com.example.laboratorandroid.game.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.laboratorandroid.R
import com.example.laboratorandroid.core.TAG
import com.example.laboratorandroid.game.data.Game
import com.example.laboratorandroid.game.game.GameEditFragment
import kotlinx.android.synthetic.main.view_game.view.*


class GameListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    var games = emptyList<Game>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onGameClick: View.OnClickListener;

    init {
        onGameClick = View.OnClickListener { view ->
            val game = view.tag as Game
            fragment.findNavController().navigate(R.id.GameEditFragment, Bundle().apply {
                putString(GameEditFragment.GAME_ID, game.id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_game, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val game = games[position]
        holder.itemView.tag = game
        holder.textView.text = game.toString()
        holder.itemView.setOnClickListener(onGameClick)
    }

    override fun getItemCount() = games.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
