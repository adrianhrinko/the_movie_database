package com.adriqueh.themoviedatabase.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adriqueh.themoviedatabase.data.api.ApiService
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import java.util.*

class MoviePagingDataSource(private val service: ApiService, private val startDate: Date?, private val endDate: Date?) :
    PagingSource<Int, ChangedMovieId>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChangedMovieId> {
        val pageNumber = params.key ?: 1
        return try {
            val response = service.getChangedMovieIds(startDate, endDate, pageNumber)
            val pagedResponse = response.body()

            val changedMovieIds = pagedResponse?.results
            val totalPages = pagedResponse?.total_pages

            val result = changedMovieIds?.filter { !it.adult }

            var nextPageNumber: Int = pageNumber

            totalPages?.let {
                if (it >= pageNumber) {
                    nextPageNumber++
                }
            }

            LoadResult.Page(
                data = result.orEmpty(),
                prevKey = null,
                nextKey = if (changedMovieIds.isNullOrEmpty()) null else nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChangedMovieId>): Int = 1

}