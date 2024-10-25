package com.eyther.lumbridge.usecase.shopping

import com.eyther.lumbridge.domain.repository.shopping.ShoppingRepository
import com.eyther.lumbridge.mapper.shopping.toUi
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import javax.inject.Inject

class GetShoppingListUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    suspend operator fun invoke(shoppingListId: Long): ShoppingListUi? {
        return shoppingRepository
            .getShoppingListById(shoppingListId)
            ?.toUi()
    }
}
