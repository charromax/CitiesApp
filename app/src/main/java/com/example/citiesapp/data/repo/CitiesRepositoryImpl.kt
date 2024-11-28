package com.example.citiesapp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.citiesapp.data.datasource.CityApi
import com.example.citiesapp.data.datasource.CityPagingSource
import com.example.citiesapp.domain.CitiesRepository
import com.example.citiesapp.domain.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(private val api: CityApi) : CitiesRepository {
    override fun fetchCities(
        query: String?,
        favouriteCities: Set<Int>,
        onlyFavourites: Boolean
    ): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { CityPagingSource(api, query, favouriteCities, onlyFavourites) }
        ).flow
    }
}