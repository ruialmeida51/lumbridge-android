package com.eyther.lumbridge.usecase.groceries

import com.eyther.lumbridge.domain.repository.groceries.GroceriesRepository
import com.eyther.lumbridge.mapper.groceries.toUi
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import javax.inject.Inject

class GetGroceriesListUseCase @Inject constructor(
    private val groceriesRepository: GroceriesRepository
) {
    suspend operator fun invoke(groceriesListId: Long): GroceriesListUi? {
        return groceriesRepository
            .getGroceriesListById(groceriesListId)
            ?.toUi()
    }
}
