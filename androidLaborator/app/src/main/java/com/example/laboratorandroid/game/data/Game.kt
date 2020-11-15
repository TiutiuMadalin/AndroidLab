package com.example.laboratorandroid.game.data

import java.time.LocalDate

class Game (
    val id: String,
    var title: String,
    var version: Double?,
    var date: LocalDate?
    ) {
        override fun toString(): String = "Title:$title Version:${version.toString()} Release Date:${date.toString()}"
}