package com.example.weatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.data.Exceptions
import com.example.weatherapp.R
import com.example.weatherapp.adapters.OnSearchItemClickListener
import com.example.weatherapp.adapters.SearchWeatherAdapter
import com.example.weatherapp.databinding.SearchFragmentBinding
import com.example.weatherapp.network.FoundCities
import com.example.weatherapp.viewModels.SearchViewModel
import com.example.weatherapp.viewModels.SearchViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var searchWeatherAdapter: SearchWeatherAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModelFactory()
        ).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)

        createAdapter()

        return binding.apply {
            searchRv.adapter = searchWeatherAdapter
            searchSv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchWeatherByCityName(query)
                    searchWeatherAdapter.notifyDataSetChanged()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createExceptionObservers()
        createDataObservers()
    }

    private fun createAdapter() {
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
    }

    private fun createDataObservers() {
        viewModel.searchedWeather.observe(viewLifecycleOwner, Observer { foundCities ->
            searchWeatherAdapter.values = foundCities
            searchWeatherAdapter.notifyDataSetChanged()
        })
    }

    private fun createExceptionObservers() {
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
    }

}
