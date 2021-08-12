package com.adriqueh.themoviedatabase.data.model

data class ChangedMovieIdsResponse(
    val results: List<ChangedMovieId>,
    val page: Int,
    val total_pages: Int,
    val total_results: Int,
) {
}