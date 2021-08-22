package com.adriqueh.themoviedatabase.ui.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adriqueh.themoviedatabase.misc.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {
    var isLoading = SingleLiveEvent<Boolean>()
    var errorMessage = SingleLiveEvent<String>()

    inline fun <T> launchAsync(
            crossinline execute: suspend () -> Response<T>,
            crossinline onSuccess: (T) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = execute()
                val result = response.body()

                if (result != null)
                    onSuccess(result)
                else
                    errorMessage.value = response.message()
            } catch (ex: Exception) {
                errorMessage.value = ex.message
            } finally {
                isLoading.value = false
            }
        }
    }

    inline fun <T> launchPagingAsync(
        crossinline execute: suspend () -> Flow<T>,
        crossinline onSuccess: (Flow<T>) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val result = execute()
                onSuccess(result)
            } catch (ex: Exception) {
                errorMessage.value = ex.message
            } finally {
                isLoading.value = false
            }
        }
    }
}