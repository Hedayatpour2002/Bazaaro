package com.example.bazaaro.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rating(
    val count: Int,
    val rate: Double
)