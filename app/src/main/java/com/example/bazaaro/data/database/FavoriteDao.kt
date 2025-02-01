package com.example.bazaaro.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bazaaro.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_table WHERE id = :productId)")
    fun isFavorite(productId: Int): Flow<Boolean>

    @Query("DELETE FROM favorite_table WHERE id = :productId")
    suspend fun removeFromFavorites(productId: Int)

    @Query("DELETE FROM favorite_table")
    suspend fun clearAllFavorites()
}