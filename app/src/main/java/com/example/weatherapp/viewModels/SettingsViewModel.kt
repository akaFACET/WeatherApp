package com.example.weatherapp.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Location
import com.example.weatherapp.LocationData

class SettingsViewModel(context: Context) : ViewModel() {
//    // TODO: Implement the ViewModel
//    val context = context
//
//    private val location = Location(context)
//
//    var currentLocation = MediatorLiveData<LocationData>()
//
//
//    fun getLocation(){
//        location.getLastLocation()
//        currentLocation.addSource(location.currentLocation, Observer {
//            currentLocation.value = it
//        })
//    }

    override fun onCleared() {
        super.onCleared()
        Log.e("err","onCleared SettingsViewModel")
    }
}
