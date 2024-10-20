package com.eyther.lumbridge.usecase.groceries

import com.eyther.lumbridge.domain.repository.groceries.GroceriesRepository
import javax.inject.Inject

class DeleteGroceriesListUseCase @Inject constructor(
    private val groceriesRepository: GroceriesRepository
) {
    suspend operator fun invoke(groceriesListId: Long) {
        groceriesRepository.deleteGroceriesList(groceriesListId)
    }
}
