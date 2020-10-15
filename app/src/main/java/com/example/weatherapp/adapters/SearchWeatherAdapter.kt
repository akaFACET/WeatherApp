package com.example.weatherapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.network.FoundCities

class SearchWeatherAdapter(
    var values: List<FoundCities>,
    val context: Context,
    val listenerSearch: OnSearchItemClickListener
): RecyclerView.Adapter<SearchWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name_tv)
        private val country: TextView = itemView.findViewById(R.id.country_tv)

        fun bind(foundCities: FoundCities, listenerSearch: OnSearchItemClickListener){
            itemView.setOnClickListener {
                listenerSearch.onItemClick(foundCities)
            }

            name.text = foundCities.cityName
            country.text = foundCities.country

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_weather_item_row,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values.get(position), listenerSearch)
    }


}