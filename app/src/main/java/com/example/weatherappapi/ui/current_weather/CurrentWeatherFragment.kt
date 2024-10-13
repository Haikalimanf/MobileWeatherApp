// com.example.weatherappapi.ui.current_weather.CurrentWeatherFragment.kt

package com.example.weatherappapi.ui.current_weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherappapi.data.repository.WeatherRepository
import com.example.weatherappapi.data.retrofit.WeatherApiConfig
import com.example.weatherappapi.databinding.FragmentCurrentWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentWeatherFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: CurrentWeatherViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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

        val repository = WeatherRepository(WeatherApiConfig.apiService)
        val factory = CurrentWeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CurrentWeatherViewModel::class.java)
        swipeRefreshLayout = binding.swipeLy
        swipeRefreshLayout.setOnRefreshListener(this)

        observeViewModel()

        checkLocationPermission()

        binding.btnSearch.setOnClickListener {
            val location = binding.etCity.text.toString().trim()
            if (location.isNotEmpty()) {
                viewModel.fetchWeather(location,"")
            } else {
                Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.weatherData.observe(viewLifecycleOwner) { weatherResponse ->
            weatherResponse?.let {
                binding.tvConditionWeather.text = it.current.condition.text
                binding.tvLocation.text = it.location.name
                binding.tvTemperature.text = "${it.current.feelslikeC}°"
                binding.tvPercentMoisture.text = "${it.current.humidity}%"
                binding.tvWindSpeed.text = "${it.current.windKph} km/h"
                binding.tvPercentRain.text = "${it.current.cloud}%"
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            Log.e("CurrentWeatherFragment", errorMessage)
        }
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
                        val latitude = location.latitude.toString()
                        val longitude = location.longitude.toString()
                        viewModel.fetchWeather(latitude, longitude)
                        Toast.makeText(requireContext(), "$latitude, $longitude", Toast.LENGTH_SHORT).show()
                        Log.d("CurrentWeatherFragment", "Location: $latitude, $longitude")
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

    override fun onRefresh() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                swipeRefreshLayout.isRefreshing = false
            },3000
        )
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }


}
