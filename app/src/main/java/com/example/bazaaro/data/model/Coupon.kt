package com.example.bazaaro.data.model

data class Coupon(
    val discountPercentage: String,
    val offerTitle: String,
    val couponCode: String,
    val daysRemaining: String
)