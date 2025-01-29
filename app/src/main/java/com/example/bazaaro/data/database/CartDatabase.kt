package com.example.bazaaro.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bazaaro.data.model.CartEntity


@Database(entities = [CartEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}