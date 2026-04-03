package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import javax.inject.Inject

class GetShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingListId: Long): ShoppingList? {
        return shoppingRepository.getShoppingListById(shoppingListId)
    }
}
