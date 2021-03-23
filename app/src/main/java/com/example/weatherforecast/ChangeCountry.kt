package com.example.weatherforecast

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*

class ChangeCountry : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_country)
        Locale.setDefault(Locale(getString(R.string.language)))
        val resources: Resources = this.resources
        val config: Configuration = resources.configuration
        config.setLocale(Locale(getString(R.string.language)))
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun changeCity(view: View) {
        var int = Intent(this, MainActivity::class.java)
        val city = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
        int.putExtra("City", city)
        setResult(Activity.RESULT_OK, int)
        this.finish()
    }
}