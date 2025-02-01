package com.example.bazaaro.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bazaaro.data.model.CartEntity
import com.example.bazaaro.data.model.FavoriteEntity


@Database(
    entities = [FavoriteEntity::class, CartEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
}