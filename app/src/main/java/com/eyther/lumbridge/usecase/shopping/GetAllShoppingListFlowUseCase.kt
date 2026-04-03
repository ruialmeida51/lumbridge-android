package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllShoppingListFlowUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingList>> = shoppingRepository.shoppingListFlow
}
