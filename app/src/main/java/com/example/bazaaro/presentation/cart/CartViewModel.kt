package com.example.bazaaro.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartState: MutableStateFlow<CartState> = MutableStateFlow(CartState.Loading)
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    init {
        getCartProducts()
    }

    fun getCartProducts() {
        viewModelScope.launch {
            _cartState.update { CartState.Loading }

            cartRepository.getAllProducts().catch { throwable ->
                _cartState.update { CartState.Error(throwable.message) }
            }.collect { cartItems ->
                if (cartItems.isEmpty()) {
                    _cartState.update { CartState.EmptyList }
                } else {
                    _cartState.update { CartState.Success(cartItems) }
                }
            }
        }
    }

    fun addToCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            cartRepository.addOrIncrementProduct(cartEntity)
        }
    }

    fun removeOrDecrementProduct(cartEntity: CartEntity) {
        viewModelScope.launch {
            cartRepository.removeOrDecrementProduct(cartEntity.id)
        }
    }

    fun removeFromCart(cartEntity: CartEntity) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartEntity.id)
        }
    }

    fun removeAllFromCart() {
        viewModelScope.launch {
            cartRepository.removeAllFromCart()
        }
    }
}