package com.adriqueh.themoviedatabase.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.adriqueh.themoviedatabase.data.api.ApiService
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.model.Movie
import com.adriqueh.themoviedatabase.data.paging.MoviePagingDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val service: ApiService
): MainRepository{
    override suspend fun getAllChangedMovieIds(startDate: Date?, endDate: Date?): Flow<PagingData<ChangedMovieId>> = Pager(
        config = PagingConfig(pageSize = 18, prefetchDistance = 1),
        pagingSourceFactory = { MoviePagingDataSource(service, startDate, endDate) }
    ).flow

    override suspend fun getMovieDetail(id: Long): Response<Movie> = service.getMovieInfo(id)

}