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
import android.widget.Toast
import java.net.URL
import com.google.gson.Gson
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private var day = true

    private val capitals = mapOf(
        "Tokyo" to Pair(35.6762f, 139.6503f),
        "Mexico City" to Pair(19.4326f, -99.1332f),
        "Lisbon" to Pair(38.7169f, -9.1399f),
        "Madrid" to Pair(40.4168f, -3.7038f),
        "Paris" to Pair(48.8566f, 2.3522f),
        "London" to Pair(51.5074f, -0.1278f),
        "Berlin" to Pair(52.5200f, 13.4050f),
        "Rome" to Pair(41.9028f, 12.4964f),
        "Amsterdam" to Pair(52.3676f, 4.9041f),
        "Brussels" to Pair(50.8503f, 4.3517f),
        "Vienna" to Pair(48.2082f, 16.3738f),
        "Bern" to Pair(46.9481f, 7.4474f),
        "Stockholm" to Pair(59.3293f, 18.0686f),
        "Oslo" to Pair(59.9139f, 10.7522f),
        "Copenhagen" to Pair(55.6761f, 12.5683f),
        "Helsinki" to Pair(60.1699f, 24.9384f),
        "Reykjavik" to Pair(64.1265f, -21.8174f),
        "Dublin" to Pair(53.3498f, -6.2603f),
        "Athens" to Pair(37.9838f, 23.7275f),
        "Warsaw" to Pair(52.2297f, 21.0122f),
        "Prague" to Pair(50.0755f, 14.4378f),
        "Budapest" to Pair(47.4979f, 19.0402f),
        "Bratislava" to Pair(48.1486f, 17.1077f),
        "Ljubljana" to Pair(46.0569f, 14.5058f),
        "Zagreb" to Pair(45.8150f, 15.9819f),
        "Sarajevo" to Pair(43.8563f, 18.4131f),
        "Belgrade" to Pair(44.7866f, 20.4489f),
        "Podgorica" to Pair(42.4304f, 19.2594f),
        "Pristina" to Pair(42.6629f, 21.1655f),
        "Skopje" to Pair(41.9981f, 21.4254f),
        "Tirana" to Pair(41.3275f, 19.8187f),
        "Sofia" to Pair(42.6977f, 23.3219f),
        "Bucharest" to Pair(44.4268f, 26.1025f),
        "Chisinau" to Pair(47.0105f, 28.8638f),
        "Kiev" to Pair(50.4501f, 30.5234f),
        "Minsk" to Pair(53.9045f, 27.5615f),
        "Riga" to Pair(56.9496f, 24.1052f),
        "Tallinn" to Pair(59.4370f, 24.7536f),
        "Vilnius" to Pair(54.6872f, 25.2797f),
        "Valletta" to Pair(35.8997f, 14.5147f),
        "Nicosia" to Pair(35.1856f, 33.3823f),
        "Andorra la Vella" to Pair(42.5063f, 1.5218f),
        "Monaco" to Pair(43.7384f, 7.4246f),
        "San Marino" to Pair(43.9424f, 12.4578f),
        "Vatican City" to Pair(41.9029f, 12.4534f),
        "Luxembourg" to Pair(49.6116f, 6.1319f),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        day = isDaytime("Europe/Lisbon")

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
            val cityInput = findViewById<EditText>(R.id.City).text.toString()

            val (lat, long) = if (cityInput.isNotBlank()) {
                getCoordinates(cityInput)
            } else {
                val lat = findViewById<EditText>(R.id.Latitude).text.toString().toFloatOrNull() ?: 38.7169f
                val long = findViewById<EditText>(R.id.Longitude).text.toString().toFloatOrNull() ?: -9.1399f
                Pair(lat, long)
            }

            fetchWeatherData(lat, long).start()
        }
    }


    private fun WeatherAPI_Call (lat: Float, long: Float) : WeatherData {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${long}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m")
            append("&timezone=auto")
        }
        val url = URL(reqString);
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

    private fun getCoordinates(input: String): Pair<Float, Float> {
        val trimmed = input.trim()
        for ((city, coords) in capitals) {
            if (city.equals(trimmed, ignoreCase = true)) {
                return coords
            }
        }
        Toast.makeText(this, "City \"$input\" not found, using Lisbon", Toast.LENGTH_SHORT).show()
        return capitals["Lisbon"]!!
    }

    private fun isDaytime(timezone: String): Boolean {
        val tz = java.util.TimeZone.getTimeZone(timezone)
        val cal = java.util.Calendar.getInstance(tz)
        val hour = cal.get(java.util.Calendar.HOUR_OF_DAY)
        return hour in 6..20
    }
    private fun updateUI(request: WeatherData) {
        runOnUiThread {

            day = isDaytime(request.timezone)


            val weatherImage: ImageView = findViewById(R.id.back)
            val weatherIcon: ImageView = findViewById(R.id.WeatherIcon)


            val pressure: TextView = findViewById(R.id.SeaLevelPresure)
            val windDirection: TextView = findViewById(R.id.WindDirection)
            val windSpeed: TextView = findViewById(R.id.WindSpeed)
            val temperature: TextView = findViewById(R.id.Temperature)
            val time: TextView = findViewById(R.id.Time)


            val latitude: EditText = findViewById(R.id.Latitude)
            val longitude: EditText = findViewById(R.id.Longitude)
            

            pressure.text = request.hourly.pressure_msl.get(12).toString() + " hPa"
            windDirection.text = request.current_weather.winddirection.toString() + " °"
            windSpeed.text = request.current_weather.windspeed.toString() + " km/h"
            temperature.text = request.current_weather.temperature.toString() + " ºC"
            time.text = request.current_weather.time
            latitude.setText(request.latitude)
            longitude.setText(request.longitude)

            val wImage = getWeatherDrawable(request.current_weather.weathercode, day)

            val res = resources
            val resID = res.getIdentifier(wImage, "drawable", packageName)
            val drawable = this.getDrawable(resID)
            weatherIcon.setImageDrawable(drawable)


            val bgImage = if (day) R.drawable.sunny_bg else R.drawable.night_bg
            weatherImage.setImageResource(bgImage)
        }
    }
}