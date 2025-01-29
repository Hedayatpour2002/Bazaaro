package com.example.bazaaro.presentation.category

sealed interface CategoryState {
    data object Loading : CategoryState
    data class Success(val categoryList: List<String>) : CategoryState
    data class Error(val errorMessage: String?) : CategoryState
}