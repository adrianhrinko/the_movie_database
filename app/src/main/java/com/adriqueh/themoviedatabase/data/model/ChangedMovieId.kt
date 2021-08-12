package com.adriqueh.themoviedatabase.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChangedMovieId(
    val id: Long,
    val adult: Boolean,
): Parcelable
