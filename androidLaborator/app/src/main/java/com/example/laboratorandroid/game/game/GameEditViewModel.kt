package com.example.laboratorandroid.game.game

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorandroid.core.TAG
import com.example.laboratorandroid.game.data.Game
import com.example.laboratorandroid.game.data.GameRepository
import kotlinx.coroutines.launch
import java.time.LocalDate


class GameEditViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val mutableGame = MutableLiveData<Game>().apply { value = Game("", "",0.0, LocalDate.now()) }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    @RequiresApi(Build.VERSION_CODES.O)
    val game: LiveData<Game> = mutableGame
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadGame(gameId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableGame.value = GameRepository.load(gameId)
                Log.i(TAG, "loadGame succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadGame failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdateGame(text: String,version: Double?, date: LocalDate?) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val game = mutableGame.value ?: return@launch
            game.title = text
            game.date=date
            game.version=version
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (game.id.isNotEmpty()) {
                    mutableGame.value = GameRepository.update(game)
                } else {
                    mutableGame.value = GameRepository.save(game)
                }
                Log.i(TAG, "saveOrUpdateGame succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdateGame failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}
