package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.mapper.shopping.toDomain
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import javax.inject.Inject

class SaveShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingList: ShoppingListUi): Long {
        return shoppingRepository.saveShoppingList(shoppingList.toDomain())
    }
}
