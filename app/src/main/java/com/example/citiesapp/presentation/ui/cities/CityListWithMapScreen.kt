package com.example.citiesapp.presentation.ui.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.citiesapp.presentation.ui.map.Map
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun CityListWithMapScreen(
    viewModel: CityViewModel = hiltViewModel()
) {
    val mapWidth = (LocalConfiguration.current.screenWidthDp / 2).dp
    val state by viewModel.state.collectAsState()

    val markerState = rememberMarkerState()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.selectedCity) {
        state.selectedCity?.let {
            markerState.position = LatLng(
                it.coord.lat,
                it.coord.lon
            )
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.coord.lat, it.coord.lon), 12f
            )
        }
    }
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {

        CityListScreen(
            modifier = Modifier.width(mapWidth),
            viewModel = viewModel,
            onCityItemClicked = viewModel::selectCity
        )

        state.selectedCity?.let { selectedCity ->
            Map(
                modifier = Modifier
                    .width(mapWidth)
                    .fillMaxHeight(),
                markerState = markerState,
                cameraPositionState = cameraPositionState
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