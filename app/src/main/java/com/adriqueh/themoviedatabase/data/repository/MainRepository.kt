package com.adriqueh.themoviedatabase.data.repository

import androidx.paging.PagingData
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.model.Movie
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*

interface MainRepository {
    suspend fun getAllChangedMovieIds(startDate: Date?, endDate: Date?): Flow<PagingData<ChangedMovieId>>
    suspend fun getMovieDetail(id:Long): Response<Movie>
}