package com.example.weatherapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment

import com.example.weatherapp.R
import com.example.weatherapp.viewModels.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.current_weather_fragment.view.*

class CurrentWeatherFragment : Fragment() {

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "CreateCurrentFragment")
        val fragmentLayout = inflater.inflate(R.layout.current_weather_fragment, container, false)

        val navController = NavHostFragment.findNavController(this)

        return fragmentLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CurrentWeatherViewModel::class.java)

    }


    override fun onDetach() {
        super.onDetach()
        Log.e("err", "DetachCurrentFragment")
    }

}
