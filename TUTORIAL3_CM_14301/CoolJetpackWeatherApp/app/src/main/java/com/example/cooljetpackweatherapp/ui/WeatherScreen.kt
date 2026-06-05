package com.example.cooljetpackweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cooljetpackweatherapp.R
import com.example.cooljetpackweatherapp.data.WMO_WeatherCode
import com.example.cooljetpackweatherapp.data.getWeatherCodeMap
import com.example.cooljetpackweatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()
    val latitude = weatherUIState.latitude
    val longitude = weatherUIState.longitude
    val temperature = weatherUIState.temperature
    val windSpeed = weatherUIState.windspeed
    val windDirection = weatherUIState.winddirection
    val weathercode = weatherUIState.weathercode
    val seaLevelPressure = weatherUIState.seaLevelPressure
    val time = weatherUIState.time

    val configuration = LocalConfiguration.current
    val day = weatherUIState.isDay

    val mapt = getWeatherCodeMap()
    val wCode = mapt[weathercode]
    val wImage = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode?.image + "day" else wCode?.image + "night"
        else -> wCode?.image
    }

    val context = LocalContext.current
    val wIcon = context.resources.getIdentifier(wImage, "drawable", context.packageName)

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            wIcon, latitude, longitude, temperature, windSpeed, windDirection,
            weathercode, seaLevelPressure, time,
            onLatitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) } },
            onLongitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) } },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    } else {
        PortraitWeatherUI(
            wIcon, latitude, longitude, temperature, windSpeed, windDirection,
            weathercode, seaLevelPressure, time,
            onLatitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLatitude(it) } },
            onLongitudeChange = { newValue -> newValue.toFloatOrNull()?.let { weatherViewModel.updateLongitude(it) } },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int,
    latitude: Float,
    longitude: Float,
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = wIcon),
            contentDescription = stringResource(id = R.string.weather_icon_desc),
            modifier = Modifier.size(130.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CoordinatesCard(
            latitude = latitude,
            longitude = longitude,
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            temperature = temperature,
            windSpeed = windSpeed,
            windDirection = windDirection,
            weathercode = weathercode,
            seaLevelPressure = seaLevelPressure,
            time = time
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUpdateButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.update_button))
        }
    }
}

@Composable
fun LandscapeWeatherUI(
    wIcon: Int,
    latitude: Float,
    longitude: Float,
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = wIcon),
                    contentDescription = stringResource(id = R.string.weather_icon_desc),
                    modifier = Modifier.size(110.dp)
                )
            }

            Box(
                modifier = Modifier.weight(1.1f)
            ) {
                CoordinatesCard(
                    latitude = latitude,
                    longitude = longitude,
                    onLatitudeChange = onLatitudeChange,
                    onLongitudeChange = onLongitudeChange
                )
            }

            Box(
                modifier = Modifier.weight(1.4f)
            ) {
                WeatherCard(
                    temperature = temperature,
                    windSpeed = windSpeed,
                    windDirection = windDirection,
                    weathercode = weathercode,
                    seaLevelPressure = seaLevelPressure,
                    time = time
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onUpdateButtonClick,
            modifier = Modifier.fillMaxWidth(0.4f)
        ) {
            Text(text = stringResource(id = R.string.update_button))
        }
    }
}