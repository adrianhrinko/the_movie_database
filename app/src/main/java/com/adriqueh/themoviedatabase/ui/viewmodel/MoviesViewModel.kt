package com.adriqueh.themoviedatabase.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.adriqueh.themoviedatabase.data.model.ChangedMovieId
import com.adriqueh.themoviedatabase.data.repository.MainRepository
import com.adriqueh.themoviedatabase.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*

class MoviesViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : BaseViewModel() {
        private lateinit var _moviesFlow: Flow<PagingData<ChangedMovieId>>
    val moviesFlow: Flow<PagingData<ChangedMovieId>>
        get() = _moviesFlow

    init {
        getAllMovies()
    }

    private fun getAllMovies(startDate: Date? = null, endDate: Date? = null) = launchPagingAsync({
        repository.getAllChangedMovieIds(startDate, endDate).cachedIn(viewModelScope)
    }, {
        _moviesFlow = it
    })
}