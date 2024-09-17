package com.example.thoitiet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

public class WeatherAdapter (
    private val weatherList: List<WeatherData>,
    private val ItemClick : (WeatherData) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivWeatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTemperature: TextView = itemView.findViewById(R.id.tvTemperature)
        val tvHumidity: TextView = itemView.findViewById(R.id.tvHumidity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val weatherData  = weatherList[position]
        holder.tvDate.text = weatherData.date
        holder.tvTemperature.text = weatherData.nhietdo
        holder.tvHumidity.text = weatherData.doam
        holder.ivWeatherIcon.setImageResource(
            if (weatherData.nang) R.drawable.img else 0
        )

        holder.itemView.setOnClickListener { ItemClick(weatherData) }

    }

    override fun getItemCount() = weatherList.size


}