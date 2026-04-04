package com.eyther.lumbridge.domain.repository.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    val shoppingListFlow: Flow<List<ShoppingList>>
    suspend fun saveShoppingList(shoppingList: ShoppingList): Long
    suspend fun deleteShoppingListById(shoppingList: Long)
    suspend fun getShoppingListById(shoppingList: Long): ShoppingList?
}
