package com.example.laboratorandroid.game.data.remote

import com.example.laboratorandroid.core.Api
import com.example.laboratorandroid.game.data.Game
import retrofit2.http.*

object GameApi {


    interface Service {
        @GET("/api/game")
        suspend fun find(): List<Game>

        @GET("/api/game/{id}")
        suspend fun read(@Path("id") gameId: String): Game;

        @Headers("Content-Type: application/json")
        @POST("/api/game")
        suspend fun create(@Body game: Game): Game

        @Headers("Content-Type: application/json")
        @PUT("/api/game/{id}")
        suspend fun update(@Path("id") gameId: String, @Body game: Game): Game
    }


    val service: Service = Api.retrofit.create(Service::class.java)
}