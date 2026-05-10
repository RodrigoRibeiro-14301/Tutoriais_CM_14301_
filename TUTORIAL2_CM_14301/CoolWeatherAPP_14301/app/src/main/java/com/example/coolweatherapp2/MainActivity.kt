package com.example.coolweatherapp2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.res.Configuration
import android.widget.ImageView
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import java.net.URL
import com.google.gson.Gson
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private var day = true

    override fun onCreate(savedInstanceState: Bundle?) {


        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                if (day) setTheme(R.style.Theme_Day)
                else setTheme(R.style.Theme_Night)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                if (day) setTheme(R.style.Theme_Day_Land)
                else setTheme(R.style.Theme_Night_Land)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val lat = findViewById<EditText>(R.id.Latitude).text.toString().toFloatOrNull() ?: 38.76f
            val long = findViewById<EditText>(R.id.Longitude).text.toString().toFloatOrNull() ?: -9.12f
            fetchWeatherData(lat, long).start()
        }
    }


    private fun WeatherAPI_Call (lat: Float, long: Float) : WeatherData {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${long}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m")
        }
        val str = reqString.toString()
        val url = URL(reqString.toString());
        url.openStream().use {
            val request = Gson().fromJson(InputStreamReader(it, "UTF-8"),WeatherData::class.java)
            return request

        }
    }
    private fun fetchWeatherData (lat: Float, long: Float) : Thread {
        return Thread {
            val weather = WeatherAPI_Call(lat,long)
            updateUI (weather)
        }
    }
    private fun updateUI(request: WeatherData) {
        runOnUiThread {
            // Imagem de fundo
            val weatherImage: ImageView = findViewById(R.id.back)
            val weatherIcon: ImageView = findViewById(R.id.WeatherIcon)

            // TextViews
            val pressure: TextView = findViewById(R.id.SeaLevelPresure)
            val windDirection: TextView = findViewById(R.id.WindDirection)
            val windSpeed: TextView = findViewById(R.id.WindSpeed)
            val temperature: TextView = findViewById(R.id.Temperature)
            val time: TextView = findViewById(R.id.Time)

            // EditTexts (Latitude e Longitude são inputs do utilizador)
            val latitude: EditText = findViewById(R.id.Latitude)
            val longitude: EditText = findViewById(R.id.Longitude)

            // Preenche os campos com os dados da API
            pressure.text = request.hourly.pressure_msl.get(12).toString() + " hPa"
            windDirection.text = request.current_weather.winddirection.toString() + " °"
            windSpeed.text = request.current_weather.windspeed.toString() + " km/h"
            temperature.text = request.current_weather.temperature.toString() + " ºC"
            time.text = request.current_weather.time
            latitude.setText(request.latitude)
            longitude.setText(request.longitude)

            // Determina o ícone do tempo
            val mapt = getWeatherCodeMap()
            val wCode = mapt.get(request.current_weather.weathercode)
            val wImage = when (wCode) {
                WMO_WeatherCode.CLEAR_SKY,
                WMO_WeatherCode.MAINLY_CLEAR,
                WMO_WeatherCode.PARTLY_CLOUDY -> if (day) wCode?.image + "day"
                else wCode?.image + "night"
                else -> wCode?.image
            }

            // Atualiza o ícone do tempo
            val res = resources
            val resID = res.getIdentifier(wImage, "drawable", packageName)
            val drawable = this.getDrawable(resID)
            weatherIcon.setImageDrawable(drawable)

            // Atualiza o fundo
            val bgImage = if (day) R.drawable.sunny_bg else R.drawable.night_bg
            weatherImage.setImageResource(bgImage)
        }
    }
}