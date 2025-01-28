package com.example.bazaaro.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val rating: Rating,
    val title: String
)