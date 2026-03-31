package com.eyther.lumbridge.domain.repository.shopping

import com.eyther.lumbridge.domain.mapper.shopping.toCached
import com.eyther.lumbridge.domain.mapper.shopping.toDomain
import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ShoppingRepository {
    val shoppingListFlow
    suspend fun saveShoppingList(shoppingList: ShoppingList): Long
    suspend fun deleteShoppingListById(shoppingList: Long)
    suspend fun getShoppingListById(shoppingList: Long): ShoppingList?
}
