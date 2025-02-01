package com.example.bazaaro.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _favoriteState: MutableStateFlow<FavoriteState> =
        MutableStateFlow(FavoriteState.Loading)
    val favoriteState: StateFlow<FavoriteState> = _favoriteState.asStateFlow()

    init {
        getAllFavorites()
    }

    fun getAllFavorites() {
        viewModelScope.launch {
            _favoriteState.update { FavoriteState.Loading }

            favoriteRepository.getAllFavorites().catch { throwable ->
                _favoriteState.update { FavoriteState.Error(throwable.message) }
            }.collect { favorites ->
                if (favorites.isEmpty()) {
                    _favoriteState.update { FavoriteState.EmptyList }
                } else {
                    _favoriteState.update { FavoriteState.Success(favorites) }
                }
            }
        }
    }


    fun toggleFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(favoriteEntity)
        }
    }


    fun clearAllFavorites() {
        viewModelScope.launch {
            favoriteRepository.clearAllFavorites()
        }
    }

    fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }
}