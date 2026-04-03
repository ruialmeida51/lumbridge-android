package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.model.shopping.ShoppingList
import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import javax.inject.Inject

class SaveShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingList: ShoppingList): Long {
        return shoppingRepository.saveShoppingList(shoppingList)
    }
}
