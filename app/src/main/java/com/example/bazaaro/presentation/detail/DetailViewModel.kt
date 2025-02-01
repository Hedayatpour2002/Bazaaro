package com.example.bazaaro.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.model.Product
import com.example.bazaaro.data.repository.CartRepository
import com.example.bazaaro.data.repository.FavoriteRepository
import com.example.bazaaro.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val productId: Int = savedStateHandle["productId"] ?: 0

    private val _detailState: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    var detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    val itemQuantity: StateFlow<Int?> = cartRepository.getItemQuantity(productId)
        .catch {
            it.printStackTrace()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    init {
        getSingleProduct()
    }

    fun getSingleProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            _detailState.emit(DetailState.Loading)
            productsRepository.getSingleProduct(productId = productId).onSuccess {
                _detailState.emit(DetailState.Success(product = it))
            }.onFailure {
                _detailState.emit(DetailState.Error(errorMessage = it.message))
            }
        }
    }

    fun addOrIncrementProduct(product: Product) {
        viewModelScope.launch {
            val cartEntity = CartEntity(
                id = product.id,
                image = product.image,
                price = product.price,
                title = product.title,
                category = product.category,
                quantity = 1,
            )

            cartRepository.addOrIncrementProduct(cartEntity)
        }
    }

    fun removeOrDecrementProduct(product: Product) {
        viewModelScope.launch {
            cartRepository.removeOrDecrementProduct(product.id)
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.removeFromCart(product.id)
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