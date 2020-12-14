package com.example.laboratorandroid.game.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
class Game (
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "title")var title: String,
    @ColumnInfo(name = "version")var version: Double?,
    @ColumnInfo(name = "date")var releaseDate: String?
    ) {
        override fun toString(): String = "Title:$title Version:${version.toString()} Release Date:$releaseDate"
}