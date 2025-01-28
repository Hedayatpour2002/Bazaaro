package com.example.bazaaro.presentation.detail

import com.example.bazaaro.data.model.Product

sealed interface DetailState {
    data object Loading : DetailState
    data class Success(val product: Product?) : DetailState
    data class Error(val errorMessage: String?) : DetailState
}