package com.adriqueh.themoviedatabase.data.api

import com.adriqueh.themoviedatabase.data.model.ChangedMovieIdsResponse
import com.adriqueh.themoviedatabase.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/changes")
    suspend fun getChangedMovieIds(@Query("start_date") startDate: String?,
                                   @Query("end_date") endDate: String?,
                                   @Query("page") page: Int,
                                   @Query("api_key") apiKey: String): Response<ChangedMovieIdsResponse>

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: Long,
                         @Query("api_key") apiKey: String): Response<Movie>
}