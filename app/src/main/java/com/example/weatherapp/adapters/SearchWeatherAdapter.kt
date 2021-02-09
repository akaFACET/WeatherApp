package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.SearchWeatherItemRowBinding
import com.example.weatherapp.data.FoundCities

class SearchWeatherAdapter(
    var values: List<FoundCities>,
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