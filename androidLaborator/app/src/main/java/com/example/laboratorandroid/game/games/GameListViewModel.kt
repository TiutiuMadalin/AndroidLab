package com.example.laboratorandroid.game.games

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



class GameListViewModel : ViewModel() {
    private val mutableGames = MutableLiveData<List<Game>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val games: LiveData<List<Game>> = mutableGames
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    @RequiresApi(Build.VERSION_CODES.O)
    fun createGame(position: Int): Unit {
        val list = mutableListOf<Game>()
        list.addAll(mutableGames.value!!)
        list.add(Game(position.toString(), "Game no $position",1.0, LocalDate.now()))
        mutableGames.value = list
    }

    fun loadGames() {
        viewModelScope.launch {
            Log.v(TAG, "loadIGames...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableGames.value = GameRepository.loadAll()
                Log.d(TAG, "loadGames succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadGames failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}
