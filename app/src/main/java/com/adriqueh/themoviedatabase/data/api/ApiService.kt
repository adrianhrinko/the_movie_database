package com.adriqueh.themoviedatabase.data.api

import com.adriqueh.themoviedatabase.data.model.ChangedMovieIdsResponse
import com.adriqueh.themoviedatabase.data.model.Movie
import retrofit2.Response
import java.util.*

interface ApiService {
    suspend fun getChangedMovieIds(startDate: Date?, endDate: Date?, page: Int): Response<ChangedMovieIdsResponse>
    suspend fun getMovieInfo(id: Long): Response<Movie>
}