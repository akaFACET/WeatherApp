package com.example.weatherapp

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.example.weatherapp.Utils.Mapper
import com.example.weatherapp.Utils.Selector
import com.example.weatherapp.Utils.Util
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.adapters.WeatherPerHour
import com.example.weatherapp.network.WeatherData

object BindingConvertions {


    @JvmStatic
    @BindingAdapter("weatherImage")
    fun ImageView.setWeatherPerDayImage(weatherPerDay: WeatherPerDay) {
        val midItem = weatherPerDay.weatherPerHour.size / 2
        setImageResource(
            Selector
                .iconPathSelector(
                    weatherPerDay.weatherPerHour[midItem].weatherId,
                    weatherPerDay.weatherPerHour[midItem].weatherIcon
                )
        )
    }

    @JvmStatic
    @BindingAdapter("weatherImage")
    fun ImageView.setWeatherPerDayImage(weatherData: WeatherData) {
        setImageResource(
            Selector
                .iconPathSelector(
                    weatherData.subWeather[0].weatherId,
                    weatherData.subWeather[0].weatherIcon
                )
        )
    }


    @JvmStatic
    @BindingAdapter("weatherImage")
    fun ImageView.setWeatherPerDayImage(weatherPerHour: WeatherPerHour?) {
        if (weatherPerHour != null) {
            setImageResource(
                Selector.iconPathSelector(
                    weatherPerHour.weatherId,
                    weatherPerHour.weatherIcon
                )
            )
        }
    }

    @JvmStatic
    fun setTemp(weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null) weatherPerHour.mainTemp.toString() else ""
    }


    @JvmStatic
    fun setTempUnits(weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null) Util.getTempUnits(weatherPerHour.units) else ""
    }

    @JvmStatic
    fun setFeelsTemp(weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            weatherPerHour.mainFeels_like.toString() + Util.getTempUnits(weatherPerHour.units)
        else
            ""
    }

    @JvmStatic
    fun setWeatherDesc(weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            weatherPerHour.weatherDescription.capitalize()
        else
            ""
    }

    @JvmStatic
    fun setPreassure(context: Context, weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            "${weatherPerHour.mainPressure} ${context.getString(R.string.millimeters)}"
        else
            ""
    }

    @JvmStatic
    fun setHumidity(context: Context, weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            "${weatherPerHour.mainHumidity} ${context.getString(R.string.percentage)}"
        else
            ""
    }

    @JvmStatic
    fun setWindSpeed(context: Context, weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            "${weatherPerHour.windSpeed} ${context.getString(R.string.metersPerSeconds)}"
        else
            ""
    }

    @JvmStatic
    fun setDateTime(weatherPerHour: WeatherPerHour?): String {
        return if (weatherPerHour != null)
            Util.getDateFromUnixTime(weatherPerHour.dt)
        else
            ""
    }


    @JvmStatic
    fun setDateWeatherPerDay(weatherPerDay: WeatherPerDay): String {
        return weatherPerDay.day
    }


    @JvmStatic
    fun setTempWeatherPerDay(weatherPerDay: WeatherPerDay): String {
        val units = Util.getTempUnits(weatherPerDay.weatherPerHour[0].units)

        val tempMax = weatherPerDay.weatherPerHour.maxBy { it ->
            it.mainTemp
        }?.mainTemp ?: ""

        val tempMin = weatherPerDay.weatherPerHour.minBy { it ->
            it.mainTemp
        }?.mainTemp ?: ""

        return "${tempMin}${units} | ${tempMax}${units}"
    }

    @JvmStatic
    fun setPlaceName(weatherData: WeatherData): String {
        return weatherData.name
    }

    @JvmStatic
    fun setDateTime(weatherData: WeatherData): String {
        return Util.getDateFromUnixTime(weatherData.updateDt)
    }

    @JvmStatic
    fun setCountry(weatherData: WeatherData): String {
        return weatherData.country
    }

    @JvmStatic
    fun setDescription(weatherData: WeatherData): String {
        return weatherData.subWeather[0].weatherDescription.capitalize()
    }

    @JvmStatic
    fun setTempUnits(weatherData: WeatherData): String {
        return Util.getTempUnits(weatherData.subWeather[0].units)
    }

    @JvmStatic
    fun setClouds(weatherData: WeatherData): String {
        return " ${weatherData.subWeather[0].cloudsAll} %"
    }

    @JvmStatic
    fun setPreassure(context: Context, weatherData: WeatherData): String {
        return " ${weatherData.subWeather[0].mainPressure} ${context.getString(R.string.millimeters)}"
    }

    @JvmStatic
    fun setHumidity(weatherData: WeatherData): String {
        return " ${weatherData.subWeather[0].mainHumidity} %"
    }

    @JvmStatic
    fun setWindSpeed(context: Context, weatherData: WeatherData): String {
        return " ${weatherData.subWeather[0].windSpeed} ${context.getString(R.string.metersPerSeconds)}"
    }

    @JvmStatic
    fun setTemp(weatherData: WeatherData): String {
        return weatherData.subWeather[0].mainTemp.toString()
    }
}