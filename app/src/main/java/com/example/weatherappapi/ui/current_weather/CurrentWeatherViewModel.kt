package com.example.weatherappapi.ui.current_weather

import androidx.lifecycle.ViewModel
import com.example.weatherappapi.data.response.WeatherResponse
import com.example.weatherappapi.data.retrofit.WeatherApiConfig
import retrofit2.Call

class CurrentWeatherViewModel : ViewModel() {

    fun fetchWeather(latitude: Double, longitude: Double): Call<WeatherResponse> {
        val apiKey = "3be62c84d2894a49a29151335240710"
        val locationQuery = "$latitude,$longitude"
        val client = WeatherApiConfig.getApiService().getWeather(apiKey, locationQuery)
        return client
    }
}