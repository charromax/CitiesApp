package com.example.citiesapp.data

import com.example.citiesapp.domain.City
import com.example.citiesapp.domain.Coordinates

data class CityDto(
    val country: String?,
    val name: String?,
    val _id: Int?,
    val coord: CoordinatesDto?,
) {
    fun toCity() =
        City(
            country = country ?: "",
            name = name ?: "",
            id = _id ?: 0,
            coord = coord?.toCoordinates() ?: Coordinates(),
        )
}

data class CoordinatesDto(
    val lon: Double?,
    val lat: Double?,
) {
    fun toCoordinates() =
        Coordinates(
            lon = lon ?: 0.0,
            lat = lat ?: 0.0,
        )
}
