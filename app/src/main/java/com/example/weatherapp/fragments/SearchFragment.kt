package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.Exceptions

import com.example.weatherapp.R
import com.example.weatherapp.adapters.OnSearchItemClickListener
import com.example.weatherapp.adapters.SearchWeatherAdapter
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.viewModels.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchWeatherAdapter: SearchWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(R.layout.search_fragment, container, false)
        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        searchWeatherAdapter = SearchWeatherAdapter(
            emptyList(),
            requireContext(),
            object : OnSearchItemClickListener {
                override fun onItemClick(foundCities: FoundCities) {
                    viewModel.saveData(foundCities)
                    findNavController().navigate(R.id.savedWeatherFragment)
                }
            }
        )

        search_rv.adapter = searchWeatherAdapter

        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            when (exception) {
                Exceptions.noInternet -> {
                    Snackbar.make(
                        search_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.noCity -> {
                    Snackbar.make(
                        search_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.others -> {
                    Snackbar.make(
                        search_fragment,
                        getString(exception.title),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        })

        viewModel.searchedWeather.observe(viewLifecycleOwner, Observer {
            searchWeatherAdapter.values = it
            searchWeatherAdapter.notifyDataSetChanged()
        })


        search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchWeatherByCityName(query)
                searchWeatherAdapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

}
