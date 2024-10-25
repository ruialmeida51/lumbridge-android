package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import javax.inject.Inject

class DeleteShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingListId: Long) {
        shoppingRepository.deleteShoppingListById(shoppingListId)
    }
}
