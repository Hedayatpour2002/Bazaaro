package com.example.bazaaro.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.repository.FavoriteRepository
import com.example.bazaaro.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    var homeState: StateFlow<HomeState> = _homeState.asStateFlow()


    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeState.emit(HomeState.Loading)
            productsRepository.getAllProducts().onSuccess {
                _homeState.emit(HomeState.Success(productList = it))
            }.onFailure {
                _homeState.emit(HomeState.Error(errorMessage = it.message))
            }
        }
    }

    fun toggleFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(favoriteEntity)
        }
    }

    fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }
}