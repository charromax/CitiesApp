package com.example.citiesapp.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.citiesapp.presentation.ui.cities.CityListScreen
import com.example.citiesapp.presentation.ui.cities.CityListWithMapScreen
import com.example.citiesapp.presentation.ui.map.MapScreen

private const val ROOT = "ROOT"
private const val MAP_ROUTE = "map/{cityName}/{lat}/{lon}"
private val CITY_NAME = "cityName"
private val LATITUDE = "lat"
private val LONGITUDE = "lon"

@Composable
fun CityAppNavHost(navController: NavHostController, isPortrait: Boolean) {

    NavHost(
        navController = navController,
        startDestination = ROOT
    ) {
        composable(ROOT) {
            if (isPortrait) {
                CityListScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onCityItemClicked = { city ->
                        navController.navigate("map/${city.name}/${city.coord.lat}/${city.coord.lon}")
                    }
                )
            } else {
                CityListWithMapScreen()
            }
        }

        composable(
            route = MAP_ROUTE,
            arguments = listOf(
                navArgument(CITY_NAME) { type = NavType.StringType },
                navArgument(LATITUDE) { type = NavType.FloatType },
                navArgument(LONGITUDE) { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString(CITY_NAME) ?: ""
            val lat = backStackEntry.arguments?.getFloat(LATITUDE)?.toDouble() ?: 0.0
            val lon = backStackEntry.arguments?.getFloat(LONGITUDE)?.toDouble() ?: 0.0
            MapScreen(
                cityName = cityName,
                lat = lat,
                lon = lon,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}