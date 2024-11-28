package com.example.citiesapp.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.citiesapp.domain.City

class CityPagingSource(
    private val api: CityApi,
    private val query: String?,
    private val favouriteCities: Set<Int> = emptySet(),
    private val onlyFavourites: Boolean = false
) : PagingSource<Int, City>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, City> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val cities = api.getCities(page, pageSize)
                .asSequence()
                .distinctBy { dto ->
                    dto._id
                }
                .filter { dto ->
                    !onlyFavourites || favouriteCities.contains(dto._id)
                }
                .filter { dto ->
                    val cityName = dto.name?.lowercase()
                    if (query.isNullOrEmpty()) true else cityName?.startsWith(query, ignoreCase = true) == true
                }
                .sortedBy { it.name?.lowercase() }
                .map { it.toCity() }
                .toList()


            val start = (page - 1) * pageSize
            val end = (start + pageSize).coerceAtMost(cities.size)

            LoadResult.Page(
                data = cities.subList(start, end),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (end == cities.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, City>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}