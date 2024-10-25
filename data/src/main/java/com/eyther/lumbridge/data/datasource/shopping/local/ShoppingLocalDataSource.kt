package com.eyther.lumbridge.data.datasource.shopping.local

import com.eyther.lumbridge.data.datasource.shopping.dao.ShoppingDao
import com.eyther.lumbridge.data.mappers.shopping.toCached
import com.eyther.lumbridge.data.mappers.shopping.toEntity
import com.eyther.lumbridge.data.model.shopping.local.ShoppingListCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ShoppingLocalDataSource @Inject constructor(
    private val shoppingDao: ShoppingDao
) {
    val shoppingListFlow: Flow<List<ShoppingListCached>> = shoppingDao.getAllShoppingLists()
        .mapNotNull { flowItem ->
            flowItem?.map { shoppingListEntity -> shoppingListEntity.toCached() }
        }

    suspend fun saveShoppingList(shoppingList: ShoppingListCached) {
        if (shoppingList.id == -1L) {
            shoppingDao.insertShoppingList(shoppingList.toEntity())
        } else {
            shoppingDao.updateShoppingList(shoppingList.toEntity().copy(shoppingListId = shoppingList.id))
        }
    }

    suspend fun deleteShoppingListById(shoppingListId: Long) {
        shoppingDao.deleteShoppingListById(shoppingListId)
    }

    suspend fun getShoppingListById(shoppingListId: Long): ShoppingListCached? {
        return shoppingDao.getShoppingListById(shoppingListId)?.toCached()
    }
}
