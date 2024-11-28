package com.example.citiesapp.presentation.ui.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.citiesapp.data.datasource.FavouritesDataStore
import com.example.citiesapp.domain.CitiesRepository
import com.example.citiesapp.domain.City
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CityViewModel"

@HiltViewModel
class CityViewModel @Inject constructor(
    private val repository: CitiesRepository,
    private val favouritesDataStore: FavouritesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(CityListState())
    val state: StateFlow<CityListState> = _state.asStateFlow()

    init {
        fetchCities()
        getFavouriteCities()
    }

    private fun getFavouriteCities() {
        viewModelScope.launch {
            favouritesDataStore.favouriteCities.collect { favourites ->
                _state.update { currentState ->
                    currentState.copy(favouriteCities = favourites)
                }
            }
        }
    }

    private fun fetchCities() {
        _state.update { currentState ->
            currentState.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val citiesFlow = repository
                    .fetchCities(
                        query = state.value.searchPrefix,
                        favouriteCities = state.value.favouriteCities,
                        onlyFavourites = state.value.onlyFavourites
                    )
                    .cachedIn(viewModelScope)

                _state.update { currentState ->
                    currentState.copy(
                        citiesPagingData = citiesFlow,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun updateSearch(newPrefix: String?) {
        _state.update { currentState ->
            currentState.copy(searchPrefix = newPrefix)
        }
        fetchCities()
    }

    fun toggleFavourite(city: City) {
        _state.update { currentState ->
            val updatedFavourites = if (isFavouriteCity(city)) {
                currentState.favouriteCities - city.id
            } else {
                currentState.favouriteCities + city.id
            }
            currentState.copy(favouriteCities = updatedFavourites)
        }
        viewModelScope.launch { favouritesDataStore.saveFavouriteCities(state.value.favouriteCities) }
    }

    fun isFavouriteCity(city: City): Boolean {
        return state.value.favouriteCities.contains(city.id)
    }

    fun showFavouritesOnly(show: Boolean) {
        _state.update { currentState ->
            currentState.copy(onlyFavourites = show)
        }
        fetchCities()
    }

    fun selectCity(city: City?) {
        _state.update { currentState ->
            currentState.copy(selectedCity = city)
        }
    }
}

data class CityListState(
    val citiesPagingData: Flow<PagingData<City>>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchPrefix: String? = null,
    val favouriteCities: Set<Int> = emptySet(),
    val onlyFavourites: Boolean = false,
    val selectedCity: City? = null
)