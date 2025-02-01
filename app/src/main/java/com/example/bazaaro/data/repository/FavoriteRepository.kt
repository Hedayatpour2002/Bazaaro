package com.example.bazaaro.data.repository

import com.example.bazaaro.data.database.FavoriteDao
import com.example.bazaaro.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
) {

    suspend fun toggleFavorite(favoriteEntity: FavoriteEntity) {
        try {
            val isFavorite = favoriteDao.isFavorite(productId = favoriteEntity.id).first()
            if (isFavorite) {
                favoriteDao.removeFromFavorites(productId = favoriteEntity.id)
            } else {
                favoriteDao.addToFavorites(favorite = favoriteEntity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllFavorites(): Flow<List<FavoriteEntity>> {
        return try {
            favoriteDao.getAllFavorites()
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(emptyList())
        }
    }

    fun isFavorite(productId: Int): Flow<Boolean> {
        return try {
            favoriteDao.isFavorite(
                productId = productId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(false)
        }
    }

    suspend fun clearAllFavorites() {
        try {
            favoriteDao.clearAllFavorites()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}