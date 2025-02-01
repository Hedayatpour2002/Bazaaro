package com.example.bazaaro.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("image") val image: String,
    @ColumnInfo("price") val price: Double,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("rating_count") val ratingCount: Int,
    @ColumnInfo("rating_rate") val ratingRate: Double,
    @ColumnInfo("category") val category: String,
)