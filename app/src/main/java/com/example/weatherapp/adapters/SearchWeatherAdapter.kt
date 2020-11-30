package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ItemWeatherPerDayBinding
import com.example.weatherapp.databinding.SearchWeatherItemRowBinding
import com.example.weatherapp.network.FoundCities

class SearchWeatherAdapter(
    var values: List<FoundCities>,
    val context: Context,
    val listenerSearch: OnSearchItemClickListener
) : RecyclerView.Adapter<SearchWeatherAdapter.ViewHolder>() {

    class ViewHolder(val binding : SearchWeatherItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foundCities: FoundCities, listenerSearch: OnSearchItemClickListener) {
            itemView.setOnClickListener {
                listenerSearch.onItemClick(foundCities)
            }
            binding.foundcities = foundCities
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchWeatherItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listenerSearch)
    }


}