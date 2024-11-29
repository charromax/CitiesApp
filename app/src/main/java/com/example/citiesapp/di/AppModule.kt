package com.example.citiesapp.di

import com.example.citiesapp.data.datasource.CityApi
import com.example.citiesapp.data.repo.CitiesRepositoryImpl
import com.example.citiesapp.domain.CitiesRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(
                "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/",
            ).addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideCityApi(retrofit: Retrofit): CityApi = retrofit.create(CityApi::class.java)

    @Provides
    fun provideCitiesRepository(api: CityApi): CitiesRepository = CitiesRepositoryImpl(api)
}
