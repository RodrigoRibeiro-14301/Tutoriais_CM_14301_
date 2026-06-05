package com.example.cooljetpackweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooljetpackweatherapp.data.WeatherApiClient
import com.example.cooljetpackweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState())
    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(lat: Float) {
        _uiState.update { it.copy(latitude = lat) }
    }

    fun updateLongitude(lon: Float) {
        _uiState.update { it.copy(longitude = lon) }
    }

    fun fetchWeather() {
        viewModelScope.launch {
            val result = WeatherApiClient.getWeather(
                _uiState.value.latitude,
                _uiState.value.longitude
            )
            result?.let { data ->
                _uiState.update {
                    it.copy(
                        temperature = data.current_weather.temperature,
                        windspeed = data.current_weather.windspeed,
                        winddirection = data.current_weather.winddirection,
                        weathercode = data.current_weather.weathercode,
                        time = data.current_weather.time,
                        isDay = data.current_weather.is_day == 1,
                        seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0.0f
                    )
                }
            }
        }
    }
}