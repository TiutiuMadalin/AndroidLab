package com.example.laboratorandroid.game.data.remote

import com.example.laboratorandroid.game.data.Game
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object GameApi {
    private const val URL = "http://192.168.0.150:3000/"

    interface Service {
        @GET("/game")
        suspend fun find(): List<Game>

        @GET("/game/{id}")
        suspend fun read(@Path("id") gameId: String): Game;

        @Headers("Content-Type: application/json")
        @POST("/game")
        suspend fun create(@Body game: Game): Game

        @Headers("Content-Type: application/json")
        @PUT("/game/{id}")
        suspend fun update(@Path("id") gameId: String, @Body game: Game): Game
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}