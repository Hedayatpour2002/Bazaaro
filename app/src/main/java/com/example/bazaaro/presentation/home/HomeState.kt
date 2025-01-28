package com.example.bazaaro.presentation.home

import com.example.bazaaro.data.model.Product

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val productList: List<Product>) : HomeState
    data class Error(val errorMessage: String?) : HomeState
}