package com.adriqueh.themoviedatabase.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.adriqueh.themoviedatabase.data.model.Movie
import com.adriqueh.themoviedatabase.data.repository.MainRepository
import com.adriqueh.themoviedatabase.misc.SingleLiveEvent
import com.adriqueh.themoviedatabase.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : BaseViewModel() {

    var movieDetail = SingleLiveEvent<Movie>()


    fun getMovieInfo(id: Long) = viewModelScope.launch {
        try {
            val result = repository.getMovieDetail(id)
            if (result.isSuccessful)
                movieDetail.value = result.body()
            else
                errorMessage.value = result.message()
        } catch (ex: Exception) {
            errorMessage.value = ex.message
        }
    }
}