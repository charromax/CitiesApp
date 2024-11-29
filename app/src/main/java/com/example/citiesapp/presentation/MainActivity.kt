package com.example.citiesapp.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.rememberNavController
import com.example.citiesapp.presentation.theme.CitiesAppTheme
import com.example.citiesapp.presentation.ui.CityAppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val configuration = LocalConfiguration.current
            val orientation = remember { mutableStateOf(configuration.orientation) }

            // Actualiza la orientación cuando cambia
            DisposableEffect(configuration) {
                orientation.value = configuration.orientation
                onDispose { }
            }

            CitiesAppTheme {
                val navController = rememberNavController()
                val isPortrait = orientation.value == Configuration.ORIENTATION_PORTRAIT
                CityAppNavHost(navController = navController, isPortrait = isPortrait)
            }
        }
    }
}
