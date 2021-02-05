package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.Utils.Selector
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.databinding.ItemWeatherPerDayBinding

class DaysScrollAdapter(
    var values: List<WeatherPerDay>,
    val context: Context,   //TODO maybe should delete, couse it doesn't uses
    val listener: OnDaysScrollItemClickListener
) : RecyclerView.Adapter<DaysScrollAdapter.ViewHolder>() {

    var selectedPosition = -1

    inner class ViewHolder(val binding: ItemWeatherPerDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherPerDay: WeatherPerDay, listener: OnDaysScrollItemClickListener) {

            itemView.isSelected = selectedPosition == layoutPosition
            itemView.setOnClickListener {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = layoutPosition
                notifyItemChanged(selectedPosition)

                listener.onItemClick(weatherPerDay.weatherPerHour)
            }
            binding.weatherperday = weatherPerDay
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherPerDayBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listener)
    }

}