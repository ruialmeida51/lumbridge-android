package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.mapper.shopping.toUi
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllShoppingListFlowUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingListUi>> =
        shoppingRepository.shoppingListFlow
            .map { it.toUi() }
}
