package com.example.bazaaro.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.Product
import com.example.bazaaro.data.repository.CartRepository
import com.example.bazaaro.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val productId: Int = savedStateHandle["productId"] ?: 0

    private val _detailState: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    var detailState: StateFlow<DetailState> = _detailState.asStateFlow()

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

    fun addToCart(product: Product) {
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

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.removeOrDecrementProduct(product.id)
        }
    }
}