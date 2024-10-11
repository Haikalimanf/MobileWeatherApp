package com.example.weatherappapi.data.retrofit

import com.example.weatherappapi.data.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
    ): Call<WeatherResponse>
}