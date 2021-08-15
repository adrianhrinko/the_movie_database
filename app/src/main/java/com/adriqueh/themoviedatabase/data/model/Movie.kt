package com.adriqueh.themoviedatabase.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val budget: Int,
    val homepage: String?,
    val id: Int,
    val genres: List<Genre>,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val revenue: Int,
    val runtime: Int?,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
): Parcelable {
    constructor(): this(false, "", 0, "", 0, listOf(), "", "", "", "", 0.0, "", "", 0, 0, "", "", "", false, 0.0, 1)
}

@Parcelize
data class Genre(
    val id: Int,
    val name: String
): Parcelable {
    constructor(): this(0, "")
}