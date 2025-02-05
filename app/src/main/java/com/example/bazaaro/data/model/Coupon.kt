package com.example.bazaaro.data.model

import java.time.LocalDate

data class Coupon(
    val discountPercentage: String,
    val offerTitle: Int,
    val couponCode: String,
    val expiryDate: LocalDate
)