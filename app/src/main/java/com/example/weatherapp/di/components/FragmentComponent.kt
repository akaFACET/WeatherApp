package com.example.weatherapp.di.components

import com.example.weatherapp.di.scopes.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }


}