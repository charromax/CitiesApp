package com.example.citiesapp.presentation.ui.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.citiesapp.domain.City

@Composable
fun CityListScreen(
    modifier: Modifier = Modifier,
    viewModel: CityViewModel = hiltViewModel(),
    onCityItemClicked: (City) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = modifier.testTag("CityListScreen"),
    ) {
        CityListContent(
            modifier = modifier,
            state = state,
            onUpdateSearch = viewModel::updateSearch,
            isFavourite = viewModel::isFavouriteCity,
            onFavouriteToggle = viewModel::toggleFavourite,
            onShowFavouritesOnly = viewModel::showFavouritesOnly,
            onCityItemClicked = onCityItemClicked,
        )
    }
}

@Composable
fun CityListContent(
    modifier: Modifier,
    state: CityListState,
    onUpdateSearch: (String?) -> Unit,
    isFavourite: (City) -> Boolean,
    onFavouriteToggle: (City) -> Unit,
    onShowFavouritesOnly: (Boolean) -> Unit,
    onCityItemClicked: (City) -> Unit,
) {
    val citiesPagingData = state.citiesPagingData?.collectAsLazyPagingItems()

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = state.searchPrefix ?: "",
            onValueChange = { newText ->
                onUpdateSearch(newText)
            },
            label = { Text("Buscar ciudad") },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            maxLines = 1,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Checkbox(
                checked = state.onlyFavourites,
                onCheckedChange = { onShowFavouritesOnly(it) },
            )
            Text(
                text = "Favoritas",
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        if (citiesPagingData != null) {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(citiesPagingData.itemCount) { index ->
                    val city = citiesPagingData[index] ?: City()
                    CityItem(
                        modifier = Modifier.testTag("CityItem-${city.name}"),
                        city = city,
                        onFavouriteToggle = { onFavouriteToggle(city) },
                        onCityItemClicked = { onCityItemClicked(city) },
                        isFavourite = isFavourite(city),
                    )
                }

                citiesPagingData.apply {
                    when {
                        loadState.refresh is LoadState.Loading ||
                            loadState.append is LoadState.Loading ||
                            state.isLoading -> {
                            item {
                                NoDataContent(
                                    text = "Cargando ciudades...",
                                    isError = false,
                                )
                            }
                        }

                        loadState.refresh is LoadState.Error || state.error != null -> {
                            val error = loadState.refresh as LoadState.Error
                            item {
                                NoDataContent(
                                    text = "Error: ${error.error.localizedMessage}",
                                    isError = true,
                                )
                            }
                        }

                        else -> {
                            if (itemCount == 0) {
                                item {
                                    NoDataContent(
                                        text = "No pude encontrar ninguna ciudad con ese nombre :(",
                                        isError = true,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CityItem(
    modifier: Modifier,
    city: City,
    onFavouriteToggle: (City) -> Unit,
    onCityItemClicked: (City) -> Unit,
    isFavourite: Boolean,
) {
    Column {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clickable { onCityItemClicked(city) },
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${city.name}, ${city.country}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Lat: ${city.coord.lat}, Lon: ${city.coord.lon}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }
            IconButton(onClick = { onFavouriteToggle(city) }) {
                Icon(
                    imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun NoDataContent(
    text: String,
    isError: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
    ) {
        if (!isError) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp),
        )
    }
}
