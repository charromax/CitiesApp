@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.citiesapp.presentation.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    cityName: String,
    lat: Double,
    lon: Double,
    showTopBar: Boolean = true,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(cityName) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    ) { pv ->
        Box(
            modifier = modifier
                .padding(pv)
        ) {
            val markerState = rememberMarkerState(position = LatLng(lat, lon))
            GoogleMap(
                modifier = modifier,
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(lat, lon), 12f)
                }
            ) {
                Marker(markerState)
            }
        }
    }
}