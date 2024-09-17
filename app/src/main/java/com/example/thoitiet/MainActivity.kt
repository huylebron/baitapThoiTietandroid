package com.example.thoitiet

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var dbHelper: WeatherDatabase
    private var weatherList = mutableListOf<WeatherData>()
    private var selectedWeatherId: Long = 0

    private lateinit var etDate: EditText
    private lateinit var etTemperature: EditText
    private lateinit var etHumidity: EditText
    private lateinit var rgWeatherType: RadioGroup
    private lateinit var btnOk: Button
    private lateinit var btnSubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = WeatherDatabase(this)

        initViews()
        setupRecyclerView()
        loadWeatherData()

        btnOk.setOnClickListener { addNewWeather() }
        btnSubmit.setOnClickListener { updateWeather() }
    }

    private fun updateWeather() {
        if (selectedWeatherId != 0L) {
            val weatherData = getWeatherDataFromInputs().copy(id = selectedWeatherId)
            val rowsAffected = dbHelper.updateWeather(weatherData)
            if (rowsAffected > 0) {
                val index = weatherList.indexOfFirst { it.id == selectedWeatherId }
                if (index != -1) {
                    weatherList[index] = weatherData
                    weatherAdapter.notifyItemChanged(index)
                }
                clearInputs()
                selectedWeatherId = 0
            }
        }

    }

    private fun addNewWeather() {
        val weatherData = getWeatherDataFromInputs()
        val id = dbHelper.insertWeather(weatherData)
        if (id != -1L) {
            weatherList.add(weatherData.copy(id = id))
            weatherAdapter.notifyItemInserted(weatherList.size - 1)
            clearInputs()
        }

    }

    private fun clearInputs() {
        etDate.text.clear()
        etTemperature.text.clear()
        etHumidity.text.clear()
        rgWeatherType.clearCheck()

    }

    private fun getWeatherDataFromInputs(): WeatherData {
        val date = etDate.text.toString()
        val nhietdo = etTemperature.text.toString()
        val doam = etHumidity.text.toString()
        val nang = rgWeatherType.checkedRadioButtonId == R.id.rbSunny
        return WeatherData(date = date, nhietdo = nhietdo, doam = doam, nang = nang)
    }

    private fun loadWeatherData() {
        weatherList.clear()
        weatherList.addAll(dbHelper.GetAll())
        weatherAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter(weatherList) { weatherData ->
            selectedWeatherId = weatherData.id
            etDate.setText(weatherData.date)
            etTemperature.setText(weatherData.nhietdo)
            etHumidity.setText(weatherData.doam)
            rgWeatherType.check(if (weatherData.nang) R.id.rbSunny else 0)
        }
        recyclerView.adapter = weatherAdapter
    }

    private fun initViews() {
        etDate = findViewById(R.id.etDate)
        etTemperature = findViewById(R.id.etTemperature)
        etHumidity = findViewById(R.id.etHumidity)
        rgWeatherType = findViewById(R.id.rgWeatherType)
        btnOk = findViewById(R.id.btnOk)
        btnSubmit = findViewById(R.id.btnSubmit)
        recyclerView = findViewById(R.id.recyclerView)
    }
}