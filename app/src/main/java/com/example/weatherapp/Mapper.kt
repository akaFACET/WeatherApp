package com.example.weatherapp

import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.network.WeatherList
import com.example.weatherapp.network.WeatherResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Mapper {


    companion object {

        fun mapWeatherResponseToWeatherData(weatherResponse: List<WeatherList>): List<WeatherData> {
            if (weatherResponse.isEmpty()){
                return emptyList()
            }else{
                val result: MutableList<WeatherData> = arrayListOf()
                weatherResponse.forEach {
                    result.add(
                        mapWeatherListToWeatherData(
                            it
                        )
                    )
                }
                return result
            }

        }

        fun mapWeatherListToWeatherData(response: WeatherList): WeatherData {
            return WeatherData(
                currentDate = getDateTime(),
                coordLon = response.coord.lon ?: Double.NaN,
                coordLat = response.coord.lat ?: Double.NaN,
                weatherId = response.weather.get(0).id ?: 0,
                weatherMain = response.weather.get(0).main ?: "NA",
                weatherDescription = response.weather.get(0).description ?: "NA",
                weatherIcon = response.weather.get(0).icon ?: "NA",
                base = response.base ?: "NA",
                mainTemp = response.main.temp?.toInt() ?: 0,
                mainFeels_like = response.main.feels_like ?: Double.NaN,
                mainPressure = hPaToMmHg(response.main.pressure),
                mainHumidity = response.main.humidity ?: 0,
                mainTemp_min = response.main.temp_min?.toInt() ?: 0,
                mainTemp_max = response.main.temp_max?.toInt() ?: 0,
                visability = response.visibility ?: 0,
                windSpeed = response.wind.speed?.toInt() ?: 0,
                windDeg = response.wind.deg ?: 0,
                cloudsAll = response.clouds.all ?: 0,
                dt = response.dt ?: 0,
                sysType = response.sys.type ?: 0,
                sysCountry = response.sys.country ?: "NA",
                sysId = response.sys.id ?: 0,
                sysSunrise = response.sys.sunrise ?: 0,
                sysSunset = response.sys.sunset ?: 0,
                timezone = response.timezone ?: 0,
                id = response.id ?: 0,
                name = response.name ?: "NA",
                cod = response.cod ?: 0
            )
        }

        private fun getDateTime(): String {
            val currentDate = Date()
            val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dateText: String = dateFormat.format(currentDate)
            val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeText: String = timeFormat.format(currentDate)
            return "$dateText $timeText"
        }

        private fun hPaToMmHg(hPa: Int?) = hPa?.div(1.333224)?.toInt() ?: 0

    }

}