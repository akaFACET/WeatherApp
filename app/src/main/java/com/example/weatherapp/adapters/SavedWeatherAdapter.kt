package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.SavedWeatherItemRowBinding
import com.example.weatherapp.data.WeatherData

class SavedWeatherAdapter(
    var values: List<WeatherData>,
    private val listenerSaved: OnSavedItemClickListener
) : RecyclerView.Adapter<SavedWeatherAdapter.ViewHolder>() {

    class ViewHolder(val binding: SavedWeatherItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherData: WeatherData, listenerSaved: OnSavedItemClickListener) {
            itemView.setOnClickListener {
                listenerSaved.onSavedItemClick(weatherData.cityId)
            }
            binding.weatherdata = weatherData
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SavedWeatherItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position], listenerSaved)
    }


}