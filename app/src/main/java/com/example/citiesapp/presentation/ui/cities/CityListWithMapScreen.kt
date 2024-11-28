package com.example.citiesapp.presentation.ui.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.citiesapp.data.datasource.FavouritesDataStore
import com.example.citiesapp.domain.City
import com.example.citiesapp.presentation.ui.map.MapScreen
import com.google.android.gms.maps.MapView

@Composable
fun CityListWithMapScreen(
    viewModel: CityViewModel = hiltViewModel()
) {
    val mapWidth = (LocalConfiguration.current.screenWidthDp / 2).dp
    val state by viewModel.state.collectAsState()

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
        // Columna con la lista de ciudades
        CityListScreen(
            modifier = Modifier.width(mapWidth),
            viewModel = viewModel,
            onCityItemClicked = viewModel::selectCity
        )
        // Columna con el mapa
        state.selectedCity?.let {
            MapScreen(
                modifier = Modifier.width(mapWidth),
                it.name,
                lat = it.coord.lat,
                lon = it.coord.lon,
                showTopBar = false,
                onBackClick = { viewModel.selectCity(null) },
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay ciudad seleccionada")
            }
        }
    }
}