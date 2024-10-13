// com.example.weatherappapi.ui.current_weather.CurrentWeatherViewModelFactory.kt

package com.example.weatherappapi.ui.current_weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappapi.data.repository.WeatherRepository

class CurrentWeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrentWeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
