package com.example.bazaaro.data.repository

import com.example.bazaaro.data.ApiService
import com.example.bazaaro.data.model.Product
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getAllProducts().body() ?: emptyList()
            Result.success(response)
        } catch (err: Exception) {
            Result.failure(err)
        }
    }

}