package com.example.weatherapp


enum class UnitsType(
    val customOrdinal: Int,
    val value: String,
    val title: String
) {
    METRIC_UNITS(
        0,
        "metric",
        "Цельсий"
    ),
    IMPERIAL_UNITS(
        1,
        "imperial",
        "Фаренгейт"
    ),
    ABSOLUTE_UNITS(
        3,
        "",
        "Кельвины"
    );

    companion object{
        fun fromValue( value: String) = UnitsType.values().firstOrNull {
            it.value == value
        } ?: getDefaultUnits()

        fun fromCustomOrdinal(ordinal: Int): UnitsType {
            return when(ordinal){
                0 -> METRIC_UNITS
                1 -> IMPERIAL_UNITS
                2 -> ABSOLUTE_UNITS
                else -> getDefaultUnits()
            }
        }

        fun getDefaultUnits(): UnitsType {
            return METRIC_UNITS
        }
    }
}