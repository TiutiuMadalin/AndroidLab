package com.example.laboratorandroid.game.game

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import com.example.laboratorandroid.game.data.local.GameDatabase
import com.example.laboratorandroid.core.Result
import com.example.laboratorandroid.core.TAG
import com.example.laboratorandroid.game.data.Game
import com.example.laboratorandroid.game.data.GameRepository

class GameEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val gameRepository: GameRepository

    init {
        val gameDao = GameDatabase.getDatabase(application, viewModelScope).gameDao()
        gameRepository = GameRepository(gameDao)
    }

    fun getGameById(gameId: String): LiveData<Game> {
        Log.v(TAG, "getItemById...")
        return gameRepository.getById(gameId)
    }

    fun saveOrUpdateGame(game: Game) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Game>
            if (game._id.isNotEmpty()) {
                result = gameRepository.update(game)
            } else {
                result = gameRepository.save(game)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}