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
import com.example.weatherapp.network.WeatherData

class SearchWeatherAdapter(
    var values: List<WeatherData>,
    val context: Context,
    val listener: OnItemClickListener
): RecyclerView.Adapter<SearchWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_iv)
        private val name: TextView = itemView.findViewById(R.id.name_tv)
        private val currentDate: TextView = itemView.findViewById(R.id.date_tv)
        private val description: TextView = itemView.findViewById(R.id.description_tv)
        private val temp: TextView = itemView.findViewById(R.id.temp_tv)
        private val country: TextView = itemView.findViewById(R.id.country_tv)
        private val windSpeed:TextView = itemView.findViewById(R.id.windSpeed_tv)
        private val humidity:TextView = itemView.findViewById(R.id.humidity_tv)
        private val pressure: TextView = itemView.findViewById(R.id.pressure_tv)
        private val clouds:TextView = itemView.findViewById(R.id.clouds_tv)

        fun bind(weatherData: WeatherData, listener: OnItemClickListener){

            itemView.setOnClickListener {
                listener.onItemClick(weatherData)
            }

            image.setImageResource(Selector
                .iconPathSelector(weatherData.weatherId,weatherData.weatherIcon))
            name.text = weatherData.name
            currentDate.text = weatherData.currentDate
            description.text = weatherData.weatherDescription
            temp.text = weatherData.mainTemp.toString()
            country.text = weatherData.sysCountry
            windSpeed.text = weatherData.windSpeed.toString() + " м/с"
            humidity.text = weatherData.mainHumidity.toString() +" %"
            pressure.text = weatherData.mainPressure.toString() + " мм"
            clouds.text = weatherData.cloudsAll.toString() + " %"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_weather_item_row,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listener)
    }


}