package com.adriqueh.themoviedatabase.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.model.Movie
import com.adriqueh.themoviedatabase.data.repository.MainRepository
import com.adriqueh.themoviedatabase.misc.SingleLiveEvent
import com.adriqueh.themoviedatabase.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : BaseViewModel() {

    private lateinit var _movieDetail: Movie
    val movieDetail: Movie
        get() = _movieDetail

    fun getMovieInfo(id: Long) = launchAsync(
            { repository.getMovieDetail(id)},
            { _movieDetail = it }
    )
}