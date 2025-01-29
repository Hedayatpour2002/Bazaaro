package com.example.bazaaro.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("image") val image: String,
    @ColumnInfo("price") val price: Double,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("quantity") val quantity: Int
)