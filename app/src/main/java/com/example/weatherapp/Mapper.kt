package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.Utils.Util.getDMFromUnixTime
import com.example.weatherapp.adapters.WeatherPerDay
import com.example.weatherapp.adapters.WeatherPerHour
import com.example.weatherapp.db.CitiesEntity
import com.example.weatherapp.db.SubWeatherEntity
import com.example.weatherapp.network.*
import java.util.*
import kotlin.time.microseconds

class Mapper {

    companion object {

        fun mapWeatherDataEntityToWeatherData(weatherDataEntity: List<WeatherDataEntity>):List<WeatherData>{
            val result: MutableList<WeatherData> = arrayListOf()
            weatherDataEntity.forEach {
                result.add(mapWeatherDataEntityToWeatherData(it))
            }
            return result
        }

        fun mapWeatherDataEntityToWeatherData(weatherDataEntity: WeatherDataEntity): WeatherData {
            return WeatherData(
                cityId = weatherDataEntity.cityId,
                isLastKnownLocation = weatherDataEntity.isLastKnownLocation,
                name = weatherDataEntity.name,
                country = weatherDataEntity.country,
                updateDt = weatherDataEntity.updateDt,
                sunrise = weatherDataEntity.sunrise,
                sunset = weatherDataEntity.sunset,
                coordLon = weatherDataEntity.coordLon,
                coordLat = weatherDataEntity.coordLat,
                subWeather = Mapper.mapSubWeatherEntityToSubWeather(weatherDataEntity.subWeatherEntity)
            )
        }

        private fun mapSubWeatherEntityToSubWeather(subWeatherEntity: List<SubWeatherEntity>):List<SubWeather>{
            val result: MutableList<SubWeather> = arrayListOf()
            subWeatherEntity.forEach {
                result.add(
                    SubWeather(
                        id = it.id,
                        cityId = it.cityId,
                        isLastKnownLocation = it.isLastKnownLocation,
                        weatherId = it.weatherId,
                        weatherMain = it.weatherMain,
                        weatherDescription = it.weatherDescription,
                        weatherIcon = it.weatherIcon,
                        dt = it.dt,
                        dt_txt = it.dt_txt,
                        mainTemp = it.mainTemp,
                        mainFeels_like = it.mainFeels_like,
                        mainTemp_min = it.mainTemp_min,
                        mainTemp_max = it.mainTemp_max,
                        mainPressure = it.mainPressure,
                        mainHumidity = it.mainHumidity,
                        windSpeed = it.windSpeed,
                        windDeg = it.windDeg,
                        cloudsAll = it.cloudsAll,
                        rain = it.rain,
                        snow = it.snow


                    )
                )
            }
            return result
        }


        fun mapSubWeatherToSubWeatherEntity(subWeather: List<SubWeather>): List<SubWeatherEntity> {
            val result: MutableList<SubWeatherEntity> = arrayListOf()
            subWeather.forEach {
                result.add(mapSubWeatherToSubWeatherEntity(it))
            }
            return result
        }

        fun mapSubWeatherToSubWeatherEntity(subWeather: SubWeather): SubWeatherEntity {
            return SubWeatherEntity(
                id = subWeather.id,
                cityId = subWeather.cityId,
                isLastKnownLocation = subWeather.isLastKnownLocation,
                weatherId = subWeather.weatherId,
                weatherMain = subWeather.weatherMain,
                weatherDescription = subWeather.weatherDescription,
                weatherIcon = subWeather.weatherIcon,
                dt = subWeather.dt,
                dt_txt = subWeather.dt_txt,
                mainTemp = subWeather.mainTemp,
                mainFeels_like = subWeather.mainFeels_like,
                mainTemp_min = subWeather.mainTemp_min,
                mainTemp_max = subWeather.mainTemp_max,
                mainPressure = subWeather.mainPressure,
                mainHumidity = subWeather.mainHumidity,
                windSpeed = subWeather.windSpeed,
                windDeg = subWeather.windDeg,
                cloudsAll = subWeather.cloudsAll,
                rain = subWeather.rain,
                snow = subWeather.snow

            )
        }

        fun mapWeatherDataToCitiesEntity(weatherData: WeatherData): CitiesEntity{
            return CitiesEntity(
                cityId = weatherData.cityId,
                isLastKnownLocation = weatherData.isLastKnownLocation,
                name = weatherData.name,
                country = weatherData.country,
                updateDt = weatherData.updateDt,
                sunrise = weatherData.sunrise,
                sunset = weatherData.sunset,
                coordLat = weatherData.coordLat,
                coordLon = weatherData.coordLon
            )
        }

        fun mapFoundCitiesResponseToFoundCities(foundCitiesResponse: FoundCitiesResponse): List<FoundCities> {

            val result: MutableList<FoundCities> = arrayListOf()
            foundCitiesResponse?.CitiesList?.forEach {
                result.add(
                    FoundCities(
                        cityId = it.id!!,
                        cityName = it.name!!,
                        country = it.sys.country
                    )
                )
            }
            return result

        }

        fun mapWeatherResponseToWeatherData(weatherResponse: WeatherResponse): WeatherData {

            val subList: MutableList<SubWeather> = arrayListOf()

            weatherResponse.list?.forEach {
                subList.add(
                    mapWeatherListToSubWeather(
                        it, weatherResponse.city.id!!
                    )
                )
            } ?: emptyArray<SubWeather>()

            //subList[0].dt = getUnixDateTime()

            return WeatherData(
                cityId = weatherResponse.city.id!!,
                isLastKnownLocation = false,
                name = weatherResponse.city.name!!,
                country = weatherResponse.city.country!!,
                updateDt = getUnixDateTime(),
                sunset = weatherResponse.city.sunset!!,
                sunrise = weatherResponse.city.sunrise!!,
                coordLon = weatherResponse.city.coord.lon!!,
                coordLat = weatherResponse.city.coord.lat!!,
                subWeather = subList
            )

        }
//        fun getWeatherPerDays (weather: List<SubWeather>): List<WeatherPerDay>{
//            var day = getDay(weather[0].dt_txt)
//            var result:MutableList<WeatherPerDay> = arrayListOf()
//            var weatherPerHour: MutableList<WeatherPerHour> = arrayListOf()
//
//            for (item in weather){
//                if (getDay(item.dt_txt) == day){
//                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
//                }else{
//                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
//                    result.add(WeatherPerDay(day.replace('-','.'),weatherPerHour))
//                    day = getDay(item.dt_txt)
//                    weatherPerHour = arrayListOf()
//                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
//                }
//            }
//            result.add(WeatherPerDay(day.replace('-','.'),weatherPerHour))
//            return result
//        }

        fun getWeatherPerDays (weather: List<SubWeather>): List<WeatherPerDay>{
            var day = getDay(weather[0].dt)
            Log.e("DATETIME", "${weather[0].dt}")
            var result:MutableList<WeatherPerDay> = arrayListOf()
            var weatherPerHour: MutableList<WeatherPerHour> = arrayListOf()

            for (item in weather){
                if (getDay(item.dt) == day){
                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
                }else{
                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
                    result.add(WeatherPerDay(day,weatherPerHour))
                    day = getDay(item.dt)
                    weatherPerHour = arrayListOf()
                    weatherPerHour.add(mapSubWeatherToWeatherPerHour(item))
                }
            }
            result.add(WeatherPerDay(day,weatherPerHour))

            result[0].weatherPerHour[0].dt = getUnixDateTime()

            return result
        }

        private fun mapSubWeatherToWeatherPerHour(subWeather: SubWeather):WeatherPerHour{
            return WeatherPerHour(
                id = subWeather.id,
                cityId =  subWeather.cityId,
                weatherId = subWeather.weatherId,
                weatherIcon = subWeather.weatherIcon,
                weatherDescription = subWeather.weatherDescription,
                weatherMain = subWeather.weatherMain,
                mainFeels_like = subWeather.mainFeels_like,
                mainHumidity = subWeather.mainHumidity,
                mainPressure = subWeather.mainPressure,
                mainTemp = subWeather.mainTemp,
                mainTemp_max = subWeather.mainTemp_max,
                mainTemp_min = subWeather.mainTemp_min,
                dt = subWeather.dt,
                dt_txt = subWeather.dt_txt.substring(11, 16),
                cloudsAll = subWeather.cloudsAll,
                windDeg = subWeather.windDeg,
                windSpeed = subWeather.windSpeed,
                precipitation = if (subWeather.rain != 0.0) subWeather.rain else subWeather.snow
            )
        }

        private fun getDay(data:Long):String{
            return getDMFromUnixTime(data)
        }

        private fun mapWeatherListToSubWeather(subWeatherList: SubWeatherList, cityId: Int): SubWeather {
            return SubWeather(
                id = 0,
                cityId = cityId,
                isLastKnownLocation = false,
                weatherId = subWeatherList.weather?.get(0)?.id ?: 0,
                weatherMain = subWeatherList.weather?.get(0)?.main ?: "NA",
                weatherDescription = subWeatherList.weather?.get(0)?.description ?: "NA",
                weatherIcon = subWeatherList.weather?.get(0)?.icon ?: "NA",
                dt = subWeatherList.dt ?: 0,
                dt_txt = subWeatherList.dt_txt ?: "NA",
                mainTemp = subWeatherList.main.temp?.toInt() ?: 0,
                mainFeels_like = subWeatherList.main.feels_like ?: Double.NaN,
                mainTemp_min = subWeatherList.main.temp_min?.toInt() ?: 0,
                mainTemp_max = subWeatherList.main.temp_max?.toInt() ?: 0,
                mainPressure = hPaToMmHg(subWeatherList.main.pressure),
                mainHumidity = subWeatherList.main.humidity ?: 0,
                windSpeed = subWeatherList.wind.speed?.toInt() ?: 0,
                windDeg = subWeatherList.wind.deg ?: 0,
                cloudsAll = subWeatherList.clouds.all ?: 0,
                rain = subWeatherList.rain?.rainVolume ?: 0.0,
                snow = subWeatherList.snow?.snowVolume ?: 0.0
            )
        }

        private fun getUnixDateTime() = Date().time.div(1000)

        private fun hPaToMmHg(hPa: Int?) = hPa?.div(1.333224)?.toInt() ?: 0




    }

}
