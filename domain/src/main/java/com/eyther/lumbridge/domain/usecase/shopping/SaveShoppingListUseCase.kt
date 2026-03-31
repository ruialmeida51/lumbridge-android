package com.eyther.lumbridge.domain.usecase.shopping

import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.domain.mapper.shopping.toDomain
import com.eyther.lumbridge.domain.model.shopping.ShoppingListUi
import javax.inject.Inject

class SaveShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingList: ShoppingListUi): Long {
        return shoppingRepository.saveShoppingList(shoppingList.toDomain())
    }
}
