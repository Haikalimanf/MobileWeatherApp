package com.example.weatherappapi.ui.current_weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappapi.data.repository.WeatherRepository
import com.example.weatherappapi.data.response.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchWeather(latitude: String, longitude: String) {
        repository.getWeather(latitude, longitude, object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    _weatherData.postValue(response.body())
                } else {
                    _error.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _error.postValue("Failure: ${t.message}")
            }
        })
    }
}