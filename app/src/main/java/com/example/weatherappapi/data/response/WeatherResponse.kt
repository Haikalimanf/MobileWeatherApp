package com.example.weatherappapi.data.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("current")
	val current: Current,

	@field:SerializedName("location")
	val location: Location
)

data class Current(

	@field:SerializedName("feelslike_c")
	val feelslikeC: Double,

	@field:SerializedName("feelslike_f")
	val feelslikeF: Double,

	@field:SerializedName("wind_degree")
	val windDegree: Int,

	@field:SerializedName("windchill_f")
	val windchillF: Double,

	@field:SerializedName("windchill_c")
	val windchillC: Double,

	@field:SerializedName("last_updated_epoch")
	val lastUpdatedEpoch: Int,

	@field:SerializedName("temp_c")
	val tempC: Double,

	@field:SerializedName("temp_f")
	val tempF: Double,

	@field:SerializedName("cloud")
	val cloud: Int,

	@field:SerializedName("wind_kph")
	val windKph: Double,

	@field:SerializedName("wind_mph")
	val windMph: Double,

	@field:SerializedName("humidity")
	val humidity: Int,

	@field:SerializedName("dewpoint_f")
	val dewpointF: Double,

	@field:SerializedName("uv")
	val uv: Double,

	@field:SerializedName("last_updated")
	val lastUpdated: String,

	@field:SerializedName("heatindex_f")
	val heatindexF: Double,

	@field:SerializedName("dewpoint_c")
	val dewpointC: Any,

	@field:SerializedName("is_day")
	val isDay: Int,

	@field:SerializedName("precip_in")
	val precipIn: Any,

	@field:SerializedName("heatindex_c")
	val heatindexC: Any,

	@field:SerializedName("wind_dir")
	val windDir: String,

	@field:SerializedName("gust_mph")
	val gustMph: Any,

	@field:SerializedName("pressure_in")
	val pressureIn: Any,

	@field:SerializedName("gust_kph")
	val gustKph: Any,

	@field:SerializedName("precip_mm")
	val precipMm: Any,

	@field:SerializedName("condition")
	val condition: Condition,

	@field:SerializedName("vis_km")
	val visKm: Any,

	@field:SerializedName("pressure_mb")
	val pressureMb: Any,

	@field:SerializedName("vis_miles")
	val visMiles: Any
)

data class Location(

	@field:SerializedName("localtime")
	val localtime: String,

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("localtime_epoch")
	val localtimeEpoch: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("lon")
	val lon: Any,

	@field:SerializedName("region")
	val region: String,

	@field:SerializedName("lat")
	val lat: Any,

	@field:SerializedName("tz_id")
	val tzId: String
)

data class Condition(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("text")
	val text: String
)
