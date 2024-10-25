package com.eyther.lumbridge.domain.repository.shopping

import com.eyther.lumbridge.data.datasource.shopping.local.ShoppingLocalDataSource
import com.eyther.lumbridge.domain.mapper.shopping.toCached
import com.eyther.lumbridge.domain.mapper.shopping.toDomain
import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoppingRepository @Inject constructor(
    private val shoppingLocalDataSource: ShoppingLocalDataSource,
    private val schedulers: Schedulers
) {
    val shoppingListFlow = shoppingLocalDataSource
        .shoppingListFlow
        .mapNotNull { it.toDomain() }

    suspend fun saveShoppingList(shoppingList: ShoppingList) = withContext(schedulers.io) {
        shoppingLocalDataSource.saveShoppingList(shoppingList.toCached())
    }

    suspend fun deleteShoppingListById(shoppingList: Long) = withContext(schedulers.io) {
        shoppingLocalDataSource.deleteShoppingListById(shoppingList)
    }

    suspend fun getShoppingListById(shoppingList: Long): ShoppingList? = withContext(schedulers.io) {
        shoppingLocalDataSource.getShoppingListById(shoppingList)?.toDomain()
    }
}
