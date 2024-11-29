package com.example.citiesapp.data.datasource

import androidx.paging.PagingSource
import com.example.citiesapp.data.CityDto
import com.example.citiesapp.data.CoordinatesDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CityPagingDataSourceTest {
    private val mockApi = mockk<CityApi>()

    @Test
    fun `carga exitosa`() =
        runTest {
            val mockCities =
                listOf(
                    CityDto(_id = 1, name = "Amsterdam", country = "NL", coord = CoordinatesDto(4.89, 52.37)),
                    CityDto(_id = 2, name = "Berlin", country = "DE", coord = CoordinatesDto(13.41, 52.52)),
                )
            coEvery { mockApi.getCities(any(), any()) } returns mockCities

            val pagingSource =
                CityPagingSource(
                    api = mockApi,
                    query = null,
                    favouriteCities = emptySet(),
                    onlyFavourites = false,
                )

            val result =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 1,
                        loadSize = 2,
                        placeholdersEnabled = false,
                    ),
                )

            val expectedCities = mockCities.map { it.toCity() }
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expectedCities,
                    prevKey = null,
                    nextKey = null,
                ),
                result,
            )
        }

    @Test
    fun `filtro por nombre`() =
        runTest {
            val mockCities =
                listOf(
                    CityDto(_id = 1, name = "Amsterdam", country = "NL", coord = CoordinatesDto(4.89, 52.37)),
                    CityDto(_id = 2, name = "Berlin", country = "DE", coord = CoordinatesDto(13.41, 52.52)),
                )
            coEvery { mockApi.getCities(any(), any()) } returns mockCities

            val pagingSource =
                CityPagingSource(
                    api = mockApi,
                    query = "Am",
                    favouriteCities = emptySet(),
                    onlyFavourites = false,
                )

            val result =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 1,
                        loadSize = 2,
                        placeholdersEnabled = false,
                    ),
                )

            val expectedCities = listOf(mockCities[0]).map { it.toCity() }
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expectedCities,
                    prevKey = null,
                    nextKey = null,
                ),
                result,
            )
        }

    @Test
    fun `filtro por favoritos`() =
        runTest {
            val mockCities =
                listOf(
                    CityDto(_id = 1, name = "Amsterdam", country = "NL", coord = CoordinatesDto(4.89, 52.37)),
                    CityDto(_id = 2, name = "Berlin", country = "DE", coord = CoordinatesDto(13.41, 52.52)),
                )
            coEvery { mockApi.getCities(any(), any()) } returns mockCities

            val pagingSource =
                CityPagingSource(
                    api = mockApi,
                    query = null,
                    favouriteCities = setOf(1),
                    onlyFavourites = true,
                )

            val result =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 1,
                        loadSize = 2,
                        placeholdersEnabled = false,
                    ),
                )

            val expectedCities = listOf(mockCities[0]).map { it.toCity() }
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expectedCities,
                    prevKey = null,
                    nextKey = null,
                ),
                result,
            )
        }

    @Test
    fun `error cuando hay una excepcion`() =
        runTest {
            coEvery { mockApi.getCities(any(), any()) } throws Exception("Network error")

            val pagingSource =
                CityPagingSource(
                    api = mockApi,
                    query = null,
                    favouriteCities = emptySet(),
                    onlyFavourites = false,
                )

            val result =
                pagingSource.load(
                    PagingSource.LoadParams.Refresh(
                        key = 1,
                        loadSize = 2,
                        placeholdersEnabled = false,
                    ),
                )

            assert(result is PagingSource.LoadResult.Error)
        }
}
