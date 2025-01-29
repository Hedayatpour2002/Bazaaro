package com.example.bazaaro.data.repository

import com.example.bazaaro.data.ApiService
import com.example.bazaaro.data.model.Product
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllCategories(): Result<List<String>> {
        return try {
            val response = apiService.getAllCategories().body() ?: emptyList()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(categoryName: String): Result<List<Product>?> {
        return try {
            val response = apiService.getProductsByCategory(categoryName = categoryName).body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}