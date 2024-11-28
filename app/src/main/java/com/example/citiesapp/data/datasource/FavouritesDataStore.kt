package com.example.citiesapp.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouritesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favourites_prefs")

    companion object {
        private val FAVOURITES_KEY = stringSetPreferencesKey("favourite_cities")
    }

    val favouriteCities: Flow<Set<Int>> = context.dataStore.data
        .map { preferences ->
            preferences[FAVOURITES_KEY]?.map {
                it.toInt()
            }?.toSet() ?: emptySet()
        }

    suspend fun saveFavouriteCities(cityIds: Set<Int>) {
        context.dataStore.edit { preferences ->
            preferences[FAVOURITES_KEY] = cityIds.map { it.toString() }.toSet()
        }
    }
}