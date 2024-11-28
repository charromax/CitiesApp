package com.example.citiesapp.data.datasource

import com.example.citiesapp.data.CityDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("cities.json")
    suspend fun getCities(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): List<CityDto>
}