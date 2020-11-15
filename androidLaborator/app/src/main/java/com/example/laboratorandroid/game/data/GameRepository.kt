package com.example.laboratorandroid.game.data

import android.util.Log
import com.example.laboratorandroid.game.data.remote.GameApi
import com.example.laboratorandroid.core.TAG

object GameRepository {
    private var cachedGames: MutableList<Game>? = null;

    suspend fun loadAll(): List<Game> {
        Log.i(TAG, "loadAll")
        if (cachedGames != null) {
            return cachedGames as List<Game>;
        }
        cachedGames = mutableListOf()
        val games = GameApi.service.find()
        cachedGames?.addAll(games)
        return cachedGames as List<Game>
    }

    suspend fun load(gameId: String): Game {
        Log.i(TAG, "load")
        val game = cachedGames?.find { it.id == gameId }
        if (game != null) {
            return game
        }
        return GameApi.service.read(gameId)
    }

    suspend fun save(game: Game): Game {
        Log.i(TAG, "save")
        val createdGame = GameApi.service.create(game)
        cachedGames?.add(createdGame)
        return createdGame
    }

    suspend fun update(game: Game): Game {
        Log.i(TAG, "update")
        val updatedGame = GameApi.service.update(game.id, game)
        val index = cachedGames?.indexOfFirst { it.id == game.id }
        if (index != null) {
            cachedGames?.set(index, updatedGame)
        }
        return updatedGame
    }
}