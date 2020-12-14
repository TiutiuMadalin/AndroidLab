package com.example.laboratorandroid.game.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.laboratorandroid.game.data.Game

@Dao
interface GameDao {
    @Query("SELECT * from games ORDER BY title ASC")
    fun getAll(): LiveData<List<Game>>

    @Query("SELECT * FROM games WHERE _id=:id ")
    fun getById(id: String): LiveData<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(game: Game)

    @Query("DELETE FROM games")
    suspend fun deleteAll()
}