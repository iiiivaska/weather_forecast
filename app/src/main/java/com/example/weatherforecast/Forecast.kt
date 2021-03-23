package com.example.weatherforecast

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class Forecast : AppCompatActivity() {

    var CITY: String = "Москва"
    var API: String = "8b9e2c439939079e0827ec1ba6cad4e5"
    var LON: String = ""
    var LAT: String = ""
    var LANG: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        CITY = intent.getStringExtra("city").toString()
        API = intent.getStringExtra("api").toString()
        LON = intent.getStringExtra("lon").toString()
        LAT = intent.getStringExtra("lat").toString()
        LANG = intent.getStringExtra("lang").toString()
        Locale.setDefault(Locale(getString(R.string.language)))
        val resources: Resources = this.resources
        val config: Configuration = resources.configuration
        config.setLocale(Locale(getString(R.string.language)))
        weatherTask().execute()
        val back_button = findViewById<ImageButton>(R.id.back)
        back_button.setOnClickListener {
            finish()
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorMessage).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/onecall?lat=$LAT&lon=$LON&exclude=minutely,hourly,alerts&appid=$API&lang=ru&units=metric").readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val current = jsonObj.getJSONObject("current")
                val updatedAt: Long = current.getLong("dt")
                val address = CITY
                val day_1 = jsonObj.getJSONArray("daily").getJSONObject(1)
                val day_2 = jsonObj.getJSONArray("daily").getJSONObject(2)
                val day_3 = jsonObj.getJSONArray("daily").getJSONObject(3)
                val day_4 = jsonObj.getJSONArray("daily").getJSONObject(4)
                val temp_1 = day_1.getJSONObject("temp")
                val temp_2 = day_2.getJSONObject("temp")
                val temp_3 = day_3.getJSONObject("temp")
                val temp_4 = day_4.getJSONObject("temp")
                val weather_1 = day_1.getJSONArray("weather").getJSONObject(0)
                val weather_2 = day_2.getJSONArray("weather").getJSONObject(0)
                val weather_3 = day_3.getJSONArray("weather").getJSONObject(0)
                val weather_4 = day_4.getJSONArray("weather").getJSONObject(0)
                val dt_1: Long = day_1.getLong("dt")
                val dt_2: Long = day_2.getLong("dt")
                val dt_3: Long = day_3.getLong("dt")
                val dt_4: Long = day_4.getLong("dt")
                val updateAtText = getString(R.string.update_at) + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val dt_str_1 = SimpleDateFormat("dd/MM", Locale.ENGLISH).format(Date(dt_1 * 1000))
                val dt_str_2 = SimpleDateFormat("dd/MM", Locale.ENGLISH).format(Date(dt_2 * 1000))
                val dt_str_3 = SimpleDateFormat("dd/MM", Locale.ENGLISH).format(Date(dt_3 * 1000))
                val dt_str_4 = SimpleDateFormat("dd/MM", Locale.ENGLISH).format(Date(dt_4 * 1000))
                val wind_speed_1 = day_1.getString("wind_speed")
                val wind_speed_2 = day_2.getString("wind_speed")
                val wind_speed_3 = day_3.getString("wind_speed")
                val wind_speed_4 = day_4.getString("wind_speed")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.update_time).text = updateAtText

                findViewById<TextView>(R.id.temp_1).text = temp_1.getString("day") + "ºC"
                findViewById<TextView>(R.id.temp_2).text = temp_2.getString("day") + "ºC"
                findViewById<TextView>(R.id.temp_3).text = temp_3.getString("day") + "ºC"
                findViewById<TextView>(R.id.temp_4).text = temp_4.getString("day") + "ºC"

                findViewById<TextView>(R.id.rain_1).text = weather_1.getString("main")
                findViewById<TextView>(R.id.rain_2).text = weather_2.getString("main")
                findViewById<TextView>(R.id.rain_3).text = weather_3.getString("main")
                findViewById<TextView>(R.id.rain_4).text = weather_4.getString("main")

                findViewById<TextView>(R.id.data_1).text = dt_str_1
                findViewById<TextView>(R.id.data_2).text = dt_str_2
                findViewById<TextView>(R.id.data_3).text = dt_str_3
                findViewById<TextView>(R.id.data_4).text = dt_str_4

                findViewById<TextView>(R.id.temp_min_1).text = getString(R.string.min_temp) + temp_1.getString("min") + "ºC"
                findViewById<TextView>(R.id.temp_min_2).text = getString(R.string.min_temp) + temp_2.getString("min") + "ºC"
                findViewById<TextView>(R.id.temp_min_3).text = getString(R.string.min_temp) + temp_3.getString("min") + "ºC"
                findViewById<TextView>(R.id.temp_min_4).text = getString(R.string.min_temp) + temp_4.getString("min") + "ºC"

                findViewById<TextView>(R.id.temp_max_1).text = getString(R.string.max_temp) + temp_1.getString("max") + "ºC"
                findViewById<TextView>(R.id.temp_max_2).text = getString(R.string.max_temp) + temp_2.getString("max") + "ºC"
                findViewById<TextView>(R.id.temp_max_3).text = getString(R.string.max_temp) + temp_3.getString("max") + "ºC"
                findViewById<TextView>(R.id.temp_max_4).text = getString(R.string.max_temp) + temp_4.getString("max") + "ºC"

                findViewById<TextView>(R.id.wind_1).text = wind_speed_1
                findViewById<TextView>(R.id.wind_2).text = wind_speed_2
                findViewById<TextView>(R.id.wind_3).text = wind_speed_3
                findViewById<TextView>(R.id.wind_4).text = wind_speed_4

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }
            catch (e: Exception)
            {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorMessage).visibility = View.VISIBLE

            }
        }
    }
}