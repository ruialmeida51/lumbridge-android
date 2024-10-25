package com.eyther.lumbridge.features.tools.shopping.viewmodel.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.shopping.model.list.ShoppingListScreenViewState
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import com.eyther.lumbridge.usecase.shopping.DeleteShoppingListUseCase
import com.eyther.lumbridge.usecase.shopping.GetAllShoppingListFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListScreenViewModel @Inject constructor(
    private val getAllShoppingListFlowUseCase: GetAllShoppingListFlowUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase
) : ViewModel(),
    IShoppingListScreenViewModel {

    companion object {
        private const val TAG = "ShoppingListScreenViewModel"
    }

    override val viewState: MutableStateFlow<ShoppingListScreenViewState> =
        MutableStateFlow(ShoppingListScreenViewState.Loading)

    init {
        fetchShoppingLists()
    }

    private fun fetchShoppingLists() {
        viewModelScope.launch {
            val shoppingListFlow = getAllShoppingListFlowUseCase()

            shoppingListFlow
                .onEach { shoppingLists ->
                    if (shoppingLists.isEmpty()) {
                        viewState.update { ShoppingListScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        ShoppingListScreenViewState.Content(
                            shoppingLists = shoppingLists.map { list ->
                                list.copy(
                                    entries = list.entries
                                )
                            }
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteShoppingList(shoppingList: ShoppingListUi) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting shopping list $shoppingList", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteShoppingListUseCase(shoppingList.id)
        }
    }
}
