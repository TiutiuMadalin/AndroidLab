package com.example.laboratorandroid.game.data


import androidx.lifecycle.LiveData
import com.example.laboratorandroid.game.data.remote.GameApi
import com.example.laboratorandroid.core.Result
import com.example.laboratorandroid.game.data.local.GameDao

class GameRepository(private val gameDao: GameDao) {

    val games = gameDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val games = GameApi.service.find()
            for (game in games) {
                gameDao.insert(game)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(gameId: String): LiveData<Game> {
        return gameDao.getById(gameId)
    }

    suspend fun save(game: Game): Result<Game> {
        try {
            val createdGame = GameApi.service.create(game)
            gameDao.insert(createdGame)
            return Result.Success(createdGame)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(game: Game): Result<Game> {
        try {
            val updatedGame = GameApi.service.update(game._id, game)
            gameDao.update(updatedGame)
            return Result.Success(updatedGame)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }
}