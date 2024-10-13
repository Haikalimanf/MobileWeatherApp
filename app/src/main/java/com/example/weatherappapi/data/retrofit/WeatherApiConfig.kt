// com.example.weatherappapi.data.retrofit.WeatherApiConfig.kt

package com.example.weatherappapi.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiConfig {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    private val retrofit: Retrofit by lazy {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    val apiService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
