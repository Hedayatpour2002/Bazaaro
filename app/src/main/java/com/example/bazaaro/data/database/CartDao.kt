package com.example.bazaaro.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bazaaro.data.model.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_table WHERE id = :id")
    suspend fun getProductById(id: Int): CartEntity?

    @Query("SELECT * FROM cart_table")
    fun getAllProducts(): Flow<List<CartEntity>>

    @Query("DELETE FROM cart_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM cart_table")
    suspend fun deleteAll()

    @Query("UPDATE cart_table SET quantity = quantity + :increment WHERE id = :productId")
    suspend fun incrementQuantity(productId: Int, increment: Int = 1)

    @Query("UPDATE cart_table SET quantity = quantity - :decrement WHERE id = :productId")
    suspend fun decrementQuantity(productId: Int, decrement: Int = 1)

    @Query("SELECT quantity FROM cart_table WHERE id = :productId")
    fun getProductQuantityById(productId: Int): Flow<Int?>
}