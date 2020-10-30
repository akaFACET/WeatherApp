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

class DaysScrollAdapter(
    var values: List<WeatherPerDay>,
    val context: Context,
    val listener: OnDaysScrollItemClickListener
) : RecyclerView.Adapter<DaysScrollAdapter.ViewHolder>() {

    var selectedPosition = -1

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image_iv)
        private val date: TextView = itemView.findViewById(R.id.date_tv)
        private val temp: TextView = itemView.findViewById(R.id.temp_tv)


        fun bind(weatherPerDay: WeatherPerDay, listener: OnDaysScrollItemClickListener) {
            if (selectedPosition == layoutPosition) {
                itemView.isSelected = true
            } else {
                itemView.isSelected = false
            }

            itemView.setOnClickListener {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)

                listener.onItemClick(weatherPerDay.weatherPerHour)
            }

            val units = Util.getTempUnits(weatherPerDay.weatherPerHour[0].units)

            val midItem = weatherPerDay.weatherPerHour.size / 2

            val tempMax = weatherPerDay.weatherPerHour.maxBy { it ->
                it.mainTemp
            }?.mainTemp ?: "NA"

            val tempMin = weatherPerDay.weatherPerHour.minBy { it ->
                it.mainTemp
            }?.mainTemp ?: "NA"

            date.text = weatherPerDay.day

            image.setImageResource(
                Selector
                    .iconPathSelector(
                        weatherPerDay.weatherPerHour[midItem].weatherId,
                        weatherPerDay.weatherPerHour[midItem].weatherIcon
                    )
            )

            temp.text = "${tempMin}${units} | ${tempMax}${units}"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_per_day, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listener)
    }
}