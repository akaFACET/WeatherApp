package com.example.weatherapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    override fun onCleared() {
        super.onCleared()
        Log.e("err","onCleared SettingsViewModel")
    }
}
