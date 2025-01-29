package com.example.bazaaro.presentation.cart

import com.example.bazaaro.data.model.CartEntity

sealed interface CartState {
    data object Loading : CartState
    data object EmptyList: CartState
    data class Success(val products: List<CartEntity>) : CartState
    data class Error(val errorMessage: String?) : CartState
}