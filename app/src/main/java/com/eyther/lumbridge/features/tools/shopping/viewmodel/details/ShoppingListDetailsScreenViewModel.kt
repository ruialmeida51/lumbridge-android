package com.eyther.lumbridge.features.tools.shopping.viewmodel.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.navigation.ToolsNavigationItem.Shopping.Companion.ARG_SHOPPING_LIST_ID
import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewEffect
import com.eyther.lumbridge.features.tools.shopping.model.details.ShoppingListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.shopping.model.details.StableShoppingListItem
import com.eyther.lumbridge.features.tools.shopping.viewmodel.details.delegate.IShoppingListDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.shopping.viewmodel.details.delegate.ShoppingListDetailsScreenInputHandler
import com.eyther.lumbridge.model.shopping.ShoppingListEntryUi
import com.eyther.lumbridge.model.shopping.ShoppingListUi
import com.eyther.lumbridge.ui.common.composables.model.input.CheckboxInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import com.eyther.lumbridge.usecase.shopping.DeleteShoppingListUseCase
import com.eyther.lumbridge.usecase.shopping.GetShoppingListUseCase
import com.eyther.lumbridge.usecase.shopping.SaveShoppingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShoppingListDetailsScreenViewModel @Inject constructor(
    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val saveShoppingListUseCase: SaveShoppingListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase,
    private val shoppingListDetailsScreenInputHandler: ShoppingListDetailsScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IShoppingListDetailsScreenViewModel,
    IShoppingListDetailsScreenInputHandler by shoppingListDetailsScreenInputHandler {

    companion object {
        private const val TAG = "ShoppingListDetailsScreenViewModel"
    }

    override val viewState: MutableStateFlow<ShoppingListDetailsScreenViewState> =
        MutableStateFlow(ShoppingListDetailsScreenViewState.Loading)

    override val viewEffects: MutableSharedFlow<ShoppingListDetailsScreenViewEffect> =
        MutableSharedFlow()

    private val shoppingListId = requireNotNull(savedStateHandle.get<Long>(ARG_SHOPPING_LIST_ID)) {
        "Shopping list ID must be provided or defaulted to -1"
    }

    init {
        viewModelScope.launch {
            inputState
                .onEach { newInputState ->
                    viewState.update {
                        ShoppingListDetailsScreenViewState.Content(newInputState)
                    }
                }
                .launchIn(this)

            loadShoppingList()
        }
    }

    private fun loadShoppingList() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error loading shopping list", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val shoppingList = getShoppingListUseCase(shoppingListId)

            if (shoppingList == null) {
                viewState.update { ShoppingListDetailsScreenViewState.Content() }
                return@launch
            }

            updateInput {
                it.copy(
                    showTickedItems = shoppingList.showTickedItems,
                    title = it.title.copy(text = shoppingList.title),
                    items = shoppingList.entries.map { shoppingListEntry ->
                        StableShoppingListItem(
                            id = UUID.randomUUID(),
                            checkboxState = CheckboxInputState(checked = shoppingListEntry.selected),
                            textInputState = TextInputState(shoppingListEntry.text)
                        )
                    }
                )
            }
        }
    }

    override fun saveShoppingList(defaultTitle: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                viewEffects.emit(ShoppingListDetailsScreenViewEffect.NavigateBack)
                Log.e(TAG, "Error saving shopping list", throwable)
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val shoppingListUi = ShoppingListUi(
                id = shoppingListId,
                showTickedItems = inputState.value.showTickedItems,
                title = inputState.value.title.text.orEmpty().ifEmpty { defaultTitle },
                entries = inputState.value.items.mapIndexed { index, stableShoppingListItem ->
                    ShoppingListEntryUi(
                        index = index,
                        text = stableShoppingListItem.textInputState.text.orEmpty(),
                        selected = stableShoppingListItem.checkboxState.checked
                    )
                }
            )

            saveShoppingListUseCase(shoppingListUi)

            viewEffects.emit(ShoppingListDetailsScreenViewEffect.NavigateBack)
        }
    }

    override fun onDeleteShoppingList() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error deleting shopping list $shoppingListId", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteShoppingListUseCase(shoppingListId)
            viewEffects.emit(ShoppingListDetailsScreenViewEffect.NavigateBack)
        }
    }
}
