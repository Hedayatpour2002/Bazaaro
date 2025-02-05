package com.example.bazaaro.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.R
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.Coupon
import com.example.bazaaro.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val fakeCouponList = MutableStateFlow(
        listOf(
            Coupon(
                discountPercentage = "15",
                offerTitle = R.string.summer_sale,
                couponCode = "SUMMER15",
                expiryDate = LocalDate.now().plusDays(10)
            ), Coupon(
                discountPercentage = "20",
                offerTitle = R.string.first_order_discount,
                couponCode = "WELCOME20",
                expiryDate = LocalDate.now().plusDays(30)
            ), Coupon(
                discountPercentage = "10",
                offerTitle = R.string.loyalty_reward,
                couponCode = "LOYALTY10",
                expiryDate = LocalDate.now().plusDays(7)
            ), Coupon(
                discountPercentage = "10",
                offerTitle = R.string.loyalty_reward,
                couponCode = "LOYALTY10",
                expiryDate = LocalDate.now().plusDays(0)
            )
        )
    )

    private val _cartState: MutableStateFlow<CartState> = MutableStateFlow(CartState.Loading)
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    private val _couponCodeInput: MutableStateFlow<String> = MutableStateFlow("")
    val couponCodeInput: StateFlow<String> = _couponCodeInput.asStateFlow()

    private val _selectedCoupon: MutableStateFlow<Coupon?> = MutableStateFlow(null)
    val selectedCoupon: StateFlow<Coupon?> = _selectedCoupon.asStateFlow()

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

    fun calculateDaysRemaining(expiryDate: LocalDate): Long {
        val currentDate = LocalDate.now()
        val daysRemaining = ChronoUnit.DAYS.between(currentDate, expiryDate)
        return daysRemaining
    }

    fun changeCouponCodeInput(couponCodeInput: String) {
        _couponCodeInput.update { couponCodeInput }
    }

    fun changeSelectedCoupon(couponCode: String) {
        val selectedCoupon = fakeCouponList.value.find { it.couponCode == couponCode }
        _selectedCoupon.update { selectedCoupon }
    }
}