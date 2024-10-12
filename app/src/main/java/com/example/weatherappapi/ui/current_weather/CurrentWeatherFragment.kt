package com.example.weatherappapi.ui.current_weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherappapi.data.response.WeatherResponse
import androidx.fragment.app.viewModels
import com.example.weatherappapi.databinding.FragmentCurrentWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        viewModel.fetchWeather(latitude,longitude).enqueue(object : Callback<WeatherResponse> {
                            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                                if (response.isSuccessful) {
                                    val weatherResponse = response.body()
                                    weatherResponse?.let {
                                        binding.tvConditionWeather.text = it.current.condition.text
                                        binding.tvLocation.text = it.location.name
                                        binding.tvTemprature.text = "${it.current.feelslikeC}°"
                                        binding.tvPercentMoisture.text = "${it.current.humidity}%"
                                        binding.tvWindSpeed.text = "${it.current.windKph} km/h"
                                        binding.tvPercentRain.text = "${it.current.cloud}%"
                                    }
                                } else {
                                    Log.e("WeatherViewModel", "Error Response: ${response.message()}")
                                }
                            }

                            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                                Log.e("WeatherViewModel", "API Failure: ${t.message}")
                            }
                        })
                    } else {
                        Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                        Log.d("CurrentWeatherFragment", "Location not found")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to retrieve location", Toast.LENGTH_SHORT).show()
                    Log.d("CurrentWeatherFragment", "Failed to retrieve location")
                }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}
