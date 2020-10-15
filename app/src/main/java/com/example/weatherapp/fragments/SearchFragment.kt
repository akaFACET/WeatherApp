package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.Exceptions
import com.example.weatherapp.LoadingStatus

import com.example.weatherapp.R
import com.example.weatherapp.adapters.OnSearchItemClickListener
import com.example.weatherapp.adapters.SearchWeatherAdapter
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.viewModels.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapterRv: SearchWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateSearchFragment")
        val fragmentLayout = inflater.inflate(R.layout.search_fragment, container, false)
        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        adapterRv = SearchWeatherAdapter(
            emptyList(),
            requireContext(),
            object : OnSearchItemClickListener{
                override fun onItemClick(foundCities: FoundCities) {
                    viewModel.saveData(foundCities)
                    findNavController().navigate(R.id.savedWeatherFragment)
                }
            }
        )

        search_rv.adapter = adapterRv


        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            when (exception) {
                Exceptions.noInternet -> {
                    Snackbar.make(
                        search_fragment,
                        "No Internet Connection",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.noCity -> {
                    Snackbar.make(
                        search_fragment,
                        "no City",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                Exceptions.others -> {
                    Snackbar.make(
                        search_fragment,
                        "Error",
                        Snackbar.LENGTH_LONG
                    ).show()
                }


            }
        })

        viewModel.searchedWeather.observe(viewLifecycleOwner, Observer {
            adapterRv.values = it
            adapterRv.notifyDataSetChanged()
        })


        search_sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchWeatherByCityName(query)
                adapterRv.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachSearchFragment")
    }
}
