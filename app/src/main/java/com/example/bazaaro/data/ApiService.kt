package com.example.bazaaro.data

import com.example.bazaaro.data.model.Product
import retrofit2.Response
import retrofit2.http.GET

const val BASE_URL = "https://fakestoreapi.com/"

interface ApiService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

}