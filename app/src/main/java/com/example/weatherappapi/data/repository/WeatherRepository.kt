package com.example.weatherappapi.data.repository

import com.example.weatherappapi.data.response.WeatherResponse
import com.example.weatherappapi.data.retrofit.WeatherApiService
import retrofit2.Callback


class WeatherRepository(private val apiService: WeatherApiService) {
    fun getWeather(latitude: String, longitude: String, callback: Callback<WeatherResponse>) {
        val apiKey = "3be62c84d2894a49a29151335240710"
        val locationQuery = "$latitude,$longitude"
        apiService.fetchWeather(
            apiKey,
            locationQuery
        ).enqueue(callback)
    }
}