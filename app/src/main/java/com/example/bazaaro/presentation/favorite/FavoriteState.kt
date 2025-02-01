package com.example.bazaaro.presentation.favorite

import com.example.bazaaro.data.model.FavoriteEntity


sealed interface FavoriteState {
    data object Loading : FavoriteState
    data object EmptyList : FavoriteState
    data class Success(val favorites: List<FavoriteEntity>) : FavoriteState
    data class Error(val errorMessage: String?) : FavoriteState
}