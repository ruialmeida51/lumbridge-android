package com.eyther.lumbridge.domain.repository.groceries

import com.eyther.lumbridge.data.datasource.groceries.local.GroceriesLocalDataSource
import com.eyther.lumbridge.domain.mapper.groceries.toCached
import com.eyther.lumbridge.domain.mapper.groceries.toDomain
import com.eyther.lumbridge.domain.model.groceries.GroceriesList
import com.eyther.lumbridge.shared.di.model.Schedulers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroceriesRepository @Inject constructor(
    private val groceriesLocalDataSource: GroceriesLocalDataSource,
    private val schedulers: Schedulers
) {
    val groceriesListFlow = groceriesLocalDataSource
        .groceriesListFlow
        .mapNotNull { it.toDomain() }

    suspend fun saveGroceriesList(groceriesList: GroceriesList) = withContext(schedulers.io) {
        groceriesLocalDataSource.saveGroceriesList(groceriesList.toCached())
    }

    suspend fun deleteGroceriesList(groceriesListId: Long) = withContext(schedulers.io) {
        groceriesLocalDataSource.deleteGroceriesList(groceriesListId)
    }

    suspend fun getGroceriesListById(groceriesListId: Long): GroceriesList? = withContext(schedulers.io) {
        groceriesLocalDataSource.getGroceriesListById(groceriesListId)?.toDomain()
    }
}
