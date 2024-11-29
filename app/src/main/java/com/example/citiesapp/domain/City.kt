package com.example.citiesapp.domain

data class City(
    val country: String = "",
    val name: String = "",
    val id: Int = 0,
    val coord: Coordinates = Coordinates(),
)

data class Coordinates(
    val lon: Double = 0.0,
    val lat: Double = 0.0,
)
