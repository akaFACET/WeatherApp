package com.example.weatherapp.data

import androidx.annotation.StringRes
import com.example.weatherapp.R
import retrofit2.HttpException
import java.net.UnknownHostException

enum class Exceptions(
    @StringRes val title: Int
) {
    NoException(R.string.noException),
    NoInternet(R.string.noInternetConnection),
    NoGPS(R.string.noGPS),
    NoCity(R.string.noCity),
    Others(R.string.error);

    companion object {

        fun getNameException(exceptions: Exceptions): Int {
            return when (exceptions) {
                NoInternet -> NoInternet.title
                NoCity -> NoCity.title
                NoGPS -> NoGPS.title
                Others -> Others.title

                else -> Others.title
            }
        }

        fun setException(throwable: Throwable): Exceptions {
            return when (throwable) {
                is HttpException -> if (throwable.code() == 200) NoCity else Others
                is UnknownHostException -> NoInternet
                else -> Others
            }
        }
    }
}