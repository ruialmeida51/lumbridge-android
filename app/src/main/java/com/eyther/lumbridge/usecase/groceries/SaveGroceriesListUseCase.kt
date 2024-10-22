package com.eyther.lumbridge.usecase.groceries

import com.eyther.lumbridge.domain.repository.groceries.GroceriesRepository
import com.eyther.lumbridge.mapper.groceries.toDomain
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import javax.inject.Inject

class SaveGroceriesListUseCase @Inject constructor(
    private val groceriesRepository: GroceriesRepository
) {
    suspend operator fun invoke(groceriesList: GroceriesListUi) {
        groceriesRepository.saveGroceriesList(groceriesList.toDomain())
    }
}
