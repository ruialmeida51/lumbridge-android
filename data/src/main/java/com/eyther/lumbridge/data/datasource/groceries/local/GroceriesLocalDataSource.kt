package com.eyther.lumbridge.data.datasource.groceries.local

import com.eyther.lumbridge.data.datasource.groceries.dao.GroceriesDao
import com.eyther.lumbridge.data.mappers.groceries.toCached
import com.eyther.lumbridge.data.mappers.groceries.toEntity
import com.eyther.lumbridge.data.model.groceries.local.GroceriesListCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GroceriesLocalDataSource @Inject constructor(
    private val groceriesDao: GroceriesDao
) {
    val groceriesListFlow: Flow<List<GroceriesListCached>> = groceriesDao.getAllGroceriesList()
        .mapNotNull { flowItem ->
            flowItem?.map { groceriesListEntity -> groceriesListEntity.toCached() }
        }

    suspend fun saveGroceriesList(groceriesList: GroceriesListCached) {
        if (groceriesList.id == -1L) {
            groceriesDao.insertGroceriesList(groceriesList.toEntity())
        } else {
            groceriesDao.updateGroceriesList(groceriesList.toEntity().copy(groceriesListId = groceriesList.id))
        }
    }

    suspend fun deleteGroceriesList(groceriesListId: Long) {
        groceriesDao.deleteGroceriesListById(groceriesListId)
    }

    suspend fun getGroceriesListById(groceriesListId: Long): GroceriesListCached? {
        return groceriesDao.getGroceriesListById(groceriesListId)?.toCached()
    }
}
