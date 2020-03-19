package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.network.WeatherData

class SavedWeatherAdapter(
    var values: List<WeatherData>,
    val context: Context,
    val listener: OnItemClickListener
): RecyclerView.Adapter<SavedWeatherAdapter.ViewHolder>() {

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
        private val updateDate:TextView = itemView.findViewById(R.id.updateDt_tv)

        fun bind(weatherData: WeatherData, listener: OnItemClickListener){

            itemView.setOnClickListener {
                listener.onItemClick(weatherData)
            }

            image.setImageResource(iconPathSelector(weatherData.weatherId,weatherData.weatherIcon))
            name.text = weatherData.name
            currentDate.text = weatherData.currentDate
            description.text = weatherData.weatherDescription
            temp.text = weatherData.mainTemp.toString()
            country.text = weatherData.sysCountry
            windSpeed.text = weatherData.windSpeed.toString() + " м/с"
            humidity.text = weatherData.mainHumidity.toString() +" %"
            pressure.text = weatherData.mainPressure.toString() + " мм"
            clouds.text = weatherData.cloudsAll.toString() + " %"
            updateDate.text = weatherData.dt.toString()

        }

        private fun iconPathSelector(id:Int, icon:String):Int{
            if (icon.contains('n')){
                when(id){
                    200 -> {return R.drawable.ic_200n}
                    201,202->{return R.drawable.ic_201n}
                    210,211,212,221-> {return R.drawable.ic_200n}
                    230,231,232->{return R.drawable.ic_230n}
                    300,301,302,310,311,312,313,314,321->{return R.drawable.ic_300n}
                    500,501 ->{return R.drawable.ic_500n}
                    502,503,504->{return R.drawable.ic_502n}
                    511->{return R.drawable.ic_511n}
                    520,521->{return R.drawable.ic_521n}
                    522,523,531->{return R.drawable.ic_522n}
                    600,601,602->{return R.drawable.ic_600n}
                    611,612,613,615,616->{return R.drawable.ic_611n}
                    620,621,622->{return R.drawable.ic_620n}
                    701,711,721 ->{return R.drawable.ic_701n}
                    731,741,751,761->{return R.drawable.ic_731n}
                    762->{return R.drawable.ic_762n}
                    771,781->{return R.drawable.ic_771n}
                    800->{return R.drawable.ic_800n}
                    801,802,803->{return R.drawable.ic_801n}
                    804->{return R.drawable.ic_804n}
                    else -> {return R.drawable.ic_na}
                }
            } else if (icon.contains('d')){
                when(id){
                    200 -> {return R.drawable.ic_200d}
                    201,202->{return R.drawable.ic_201d}
                    210,211,212,221-> {return R.drawable.ic_200d}
                    230,231,232->{return R.drawable.ic_230d}
                    300,301,302,310,311,312,313,314,321->{return R.drawable.ic_300d}
                    500,501 ->{return R.drawable.ic_500d}
                    502,503,504->{return R.drawable.ic_502d}
                    511->{return R.drawable.ic_511d}
                    520,521->{return R.drawable.ic_521d}
                    522,523,531->{return R.drawable.ic_522d}
                    600,601,602->{return R.drawable.ic_600d}
                    611,612,613,615,616->{return R.drawable.ic_611d}
                    620,621,622->{return R.drawable.ic_620d}
                    701,711,721 ->{return R.drawable.ic_701d}
                    731,741,751,761->{return R.drawable.ic_731d}
                    762->{return R.drawable.ic_762d}
                    771,781->{return R.drawable.ic_771d}
                    800->{return R.drawable.ic_800d}
                    801,802,803->{return R.drawable.ic_801d}
                    804->{return R.drawable.ic_804d}
                    else -> {return R.drawable.ic_na}
                }
            }else
                return R.drawable.ic_na
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