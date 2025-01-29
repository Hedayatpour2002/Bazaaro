package com.example.bazaaro.data.repository

import com.example.bazaaro.data.database.CartDao
import com.example.bazaaro.data.model.CartEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    suspend fun addOrIncrementProduct(cartEntity: CartEntity) {
        try {
            val existingProduct = cartDao.getProductById(cartEntity.id)

            if (existingProduct == null) {
                cartDao.insert(cartEntity)
            } else {
                cartDao.incrementQuantity(cartEntity.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeOrDecrementProduct(productId: Int) {
        try {
            val existingProduct = cartDao.getProductQuantityById(productId).first() ?: return

            if (existingProduct == 1) {
                cartDao.delete(productId)
            } else {
                cartDao.decrementQuantity(productId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getItemQuantity(productId: Int): Flow<Int?> {
        return cartDao.getProductQuantityById(productId)
    }

    fun getAllProducts(): Flow<List<CartEntity>> {
        return try {
            cartDao.getAllProducts()
        } catch (e: Exception) {
            e.printStackTrace()
            flowOf(emptyList())
        }
    }

    suspend fun removeFromCart(productId: Int) {
        try {
            cartDao.delete(productId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeAllFromCart() {
        try {
            cartDao.deleteAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}