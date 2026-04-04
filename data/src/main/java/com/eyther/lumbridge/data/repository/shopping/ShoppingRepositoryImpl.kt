package com.eyther.lumbridge.data.repository.shopping

import com.eyther.lumbridge.data.datasource.shopping.local.ShoppingLocalDataSource
import com.eyther.lumbridge.data.mapper.shopping.toCached
import com.eyther.lumbridge.data.mapper.shopping.toDomain
import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingLocalDataSource: ShoppingLocalDataSource,
    private val schedulers: Schedulers
) : ShoppingRepository {

    override val shoppingListFlow = shoppingLocalDataSource
        .shoppingListFlow
        .map { it.toDomain() }

    override suspend fun saveShoppingList(shoppingList: ShoppingList): Long = withContext(schedulers.io) {
        shoppingLocalDataSource.saveShoppingList(shoppingList.toCached())
    }

    override suspend fun deleteShoppingListById(shoppingList: Long) = withContext(schedulers.io) {
        shoppingLocalDataSource.deleteShoppingListById(shoppingList)
    }

    override suspend fun getShoppingListById(shoppingList: Long): ShoppingList? = withContext(schedulers.io) {
        shoppingLocalDataSource.getShoppingListById(shoppingList)?.toDomain()
    }
}
