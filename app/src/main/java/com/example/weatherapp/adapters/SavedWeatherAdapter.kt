package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.Selector
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.network.WeatherData

class SavedWeatherAdapter(
    var values: List<WeatherData>,
    val context: Context,
    val listenerSaved: OnSavedItemClickListener
) : RecyclerView.Adapter<SavedWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_iv)
        private val name: TextView = itemView.findViewById(R.id.place_tv)
        private val currentDate: TextView = itemView.findViewById(R.id.date_tv)
        private val description: TextView = itemView.findViewById(R.id.description_tv)
        private val temp: TextView = itemView.findViewById(R.id.temp_tv)
        private val country: TextView = itemView.findViewById(R.id.country_tv)
        private val windSpeed: TextView = itemView.findViewById(R.id.windSpeed_tv)
        private val humidity: TextView = itemView.findViewById(R.id.humidity_tv)
        private val pressure: TextView = itemView.findViewById(R.id.pressure_tv)
        private val clouds: TextView = itemView.findViewById(R.id.clouds_tv)
        private val tempUnits: TextView = itemView.findViewById(R.id.tempDesc_tv)

        fun bind(weatherData: WeatherData, listenerSaved: OnSavedItemClickListener) {

            itemView.setOnClickListener {
                listenerSaved.onSavedItemClick(weatherData.cityId)
            }

            image.setImageResource(
                Selector
                    .iconPathSelector(
                        weatherData.subWeather[0].weatherId,
                        weatherData.subWeather[0].weatherIcon
                    )
            )
            name.text = weatherData.name
            currentDate.text = Util.getDateFromUnixTime(weatherData.updateDt)
            description.text = weatherData.subWeather[0].weatherDescription.capitalize()
            temp.text = weatherData.subWeather[0].mainTemp.toString()
            country.text = weatherData.country
            windSpeed.text = " " + weatherData.subWeather[0].windSpeed.toString() + " м/с"
            humidity.text = " " + weatherData.subWeather[0].mainHumidity.toString() + " %"
            pressure.text = " " + weatherData.subWeather[0].mainPressure.toString() + " мм"
            clouds.text = " " + weatherData.subWeather[0].cloudsAll.toString() + " %"
            tempUnits.text = Util.getTempUnits(weatherData.subWeather[0].units)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_weather_item_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listenerSaved)
    }


}