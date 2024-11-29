package com.example.citiesapp.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {
    fun fetchCities(
        query: String?,
        favouriteCities: Set<Int>,
        onlyFavourites: Boolean,
    ): Flow<PagingData<City>>
}
