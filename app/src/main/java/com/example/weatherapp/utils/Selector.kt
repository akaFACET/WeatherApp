package com.example.weatherapp.utils

import com.example.weatherapp.R

class Selector {
    companion object {
        fun iconPathSelector(id: Int, icon: String): Int {
            if (icon.contains('n')) {
                when (id) {
                    200 -> {
                        return R.drawable.ic_200n
                    }
                    201, 202 -> {
                        return R.drawable.ic_201n
                    }
                    210, 211, 212, 221 -> {
                        return R.drawable.ic_200n
                    }
                    230, 231, 232 -> {
                        return R.drawable.ic_230n
                    }
                    300, 301, 302, 310, 311, 312, 313, 314, 321 -> {
                        return R.drawable.ic_300n
                    }
                    500, 501 -> {
                        return R.drawable.ic_500n
                    }
                    502, 503, 504 -> {
                        return R.drawable.ic_502n
                    }
                    511 -> {
                        return R.drawable.ic_511n
                    }
                    520, 521 -> {
                        return R.drawable.ic_521n
                    }
                    522, 523, 531 -> {
                        return R.drawable.ic_522n
                    }
                    600, 601, 602 -> {
                        return R.drawable.ic_600n
                    }
                    611, 612, 613, 615, 616 -> {
                        return R.drawable.ic_611n
                    }
                    620, 621, 622 -> {
                        return R.drawable.ic_620n
                    }
                    701, 711, 721 -> {
                        return R.drawable.ic_701n
                    }
                    731, 741, 751, 761 -> {
                        return R.drawable.ic_731n
                    }
                    762 -> {
                        return R.drawable.ic_762n
                    }
                    771, 781 -> {
                        return R.drawable.ic_771n
                    }
                    800 -> {
                        return R.drawable.ic_800n
                    }
                    801, 802, 803 -> {
                        return R.drawable.ic_801n
                    }
                    804 -> {
                        return R.drawable.ic_804n
                    }
                    else -> {
                        return R.drawable.ic_na
                    }
                }
            } else if (icon.contains('d')) {
                when (id) {
                    200 -> {
                        return R.drawable.ic_200d
                    }
                    201, 202 -> {
                        return R.drawable.ic_201d
                    }
                    210, 211, 212, 221 -> {
                        return R.drawable.ic_200d
                    }
                    230, 231, 232 -> {
                        return R.drawable.ic_230d
                    }
                    300, 301, 302, 310, 311, 312, 313, 314, 321 -> {
                        return R.drawable.ic_300d
                    }
                    500, 501 -> {
                        return R.drawable.ic_500d
                    }
                    502, 503, 504 -> {
                        return R.drawable.ic_502d
                    }
                    511 -> {
                        return R.drawable.ic_511d
                    }
                    520, 521 -> {
                        return R.drawable.ic_521d
                    }
                    522, 523, 531 -> {
                        return R.drawable.ic_522d
                    }
                    600, 601, 602 -> {
                        return R.drawable.ic_600d
                    }
                    611, 612, 613, 615, 616 -> {
                        return R.drawable.ic_611d
                    }
                    620, 621, 622 -> {
                        return R.drawable.ic_620d
                    }
                    701, 711, 721 -> {
                        return R.drawable.ic_701d
                    }
                    731, 741, 751, 761 -> {
                        return R.drawable.ic_731d
                    }
                    762 -> {
                        return R.drawable.ic_762d
                    }
                    771, 781 -> {
                        return R.drawable.ic_771d
                    }
                    800 -> {
                        return R.drawable.ic_800d
                    }
                    801, 802, 803 -> {
                        return R.drawable.ic_801d
                    }
                    804 -> {
                        return R.drawable.ic_804d
                    }
                    else -> {
                        return R.drawable.ic_na
                    }
                }
            } else
                return R.drawable.ic_na
        }
    }
}