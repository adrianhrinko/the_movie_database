package com.adriqueh.themoviedatabase.data.api

import com.adriqueh.themoviedatabase.BuildConfig
import com.adriqueh.themoviedatabase.data.model.ChangedMovieIdsResponse
import com.adriqueh.themoviedatabase.data.model.Movie
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val api: MovieApi
    ): ApiService {

    override suspend fun getChangedMovieIds(
        startDate: Date?,
        endDate: Date?,
        page: Int
    ): Response<ChangedMovieIdsResponse> = with(SimpleDateFormat("dd-MM-yyyy")) {
        api.getChangedMovieIds(startDate?.let { format(it) } , endDate?.let { format(it) },
                page, BuildConfig.API_KEY)
    }


    override suspend fun getMovieInfo(id: Long): Response<Movie> = api.getMovie(id, BuildConfig.API_KEY)
}