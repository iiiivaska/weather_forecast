package com.example.weatherforecast

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

var CITY: String = "Москва"

class MainActivity : AppCompatActivity() {

    val API: String = "8b9e2c439939079e0827ec1ba6cad4e5"
    var LON: String = ""
    var LAT: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherTask().execute()
        val button = findViewById<ImageButton>(R.id.changeCity)
        button.setOnClickListener{
            val intent = Intent(this, ChangeCountry::class.java)
            intent.putExtra("mainActivity",this.toString())
            startActivityForResult(intent, 1)
        }
        val forecast_button = findViewById<Button>(R.id.forecast_button)
        forecast_button.setOnClickListener{
            val int = Intent(this, Forecast::class.java)
            int.putExtra("city", CITY)
            int.putExtra("api", API)
            int.putExtra("lon", LON)
            int.putExtra("lat", LAT)
            int.putExtra("lang", getString(R.string.language))
            startActivity(int)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null){
            return
        }
        CITY = data.getStringExtra("City").toString()
        weatherTask().execute()
    }

    fun changeLanguage(view: View){
        if (findViewById<TextView>(R.id.sunrise_text).text == "Sunrise") {
            val locale = Locale("RU")
            Locale.setDefault(locale)
            val resources: Resources = this.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            this.recreate()
        }
        else
        {
            val locale = Locale("EN")
            Locale.setDefault(locale)
            val resources: Resources = this.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            this.recreate()
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
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API&lang=${getString(R.string.language)}").readText(Charsets.UTF_8)
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
                val coord = jsonObj.getJSONObject("coord")
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weathe = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updateAtText = getString(R.string.update_at) + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                )
                val temp = main.getString("temp") + "ºC"
                val tempMin = getString(R.string.min_temp) + main.getString("temp_min") + "ºC"
                val tempMax = getString(R.string.max_temp) + main.getString("temp_max") + "ºC"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val windSide = wind.getString("deg") + "º"
                val weatherDescription = weathe.getString("description")
                val address = jsonObj.getString("name") + ", "+sys.getString("country")

                LON = coord.getString("lon")
                LAT = coord.getString("lat")
                findViewById<TextView>(R.id.address).text = address
                CITY = address
                findViewById<TextView>(R.id.update_time).text = updateAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(sunrise * 1000)
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(sunset * 1000)
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text =  humidity
                findViewById<TextView>(R.id.wind_side).text = windSide

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }
            catch (e: Exception)
            {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }
        }

    }
}