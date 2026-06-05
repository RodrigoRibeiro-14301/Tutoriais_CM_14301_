package com.example.cooljetpackweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cooljetpackweatherapp.R

@Composable
fun WeatherCard(
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherRow(stringResource(id = R.string.sea_level_pressure), "$seaLevelPressure hPa")
            WeatherRow(stringResource(id = R.string.wind_direction), "$windDirection°")
            WeatherRow(stringResource(id = R.string.wind_speed), "$windSpeed km/h")
            WeatherRow(stringResource(id = R.string.temperature_label), "$temperature°C")
            WeatherRow(stringResource(id = R.string.time_label), time)
        }
    }
}