package com.eyther.lumbridge.usecase.groceries

import com.eyther.lumbridge.domain.repository.groceries.GroceriesRepository
import com.eyther.lumbridge.mapper.groceries.toUi
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllGroceriesListFlowUseCase @Inject constructor(
    private val groceriesRepository: GroceriesRepository
) {
    operator fun invoke(): Flow<List<GroceriesListUi>> =
        groceriesRepository.groceriesListFlow
            .map { it.toUi() }
}
