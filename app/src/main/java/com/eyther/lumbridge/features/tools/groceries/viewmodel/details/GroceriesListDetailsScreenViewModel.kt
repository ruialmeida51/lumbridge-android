package com.eyther.lumbridge.features.tools.groceries.viewmodel.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.groceries.model.details.GroceriesListDetailsScreenInputState
import com.eyther.lumbridge.features.tools.groceries.model.details.GroceriesListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.groceries.model.details.StableGroceriesListItem
import com.eyther.lumbridge.features.tools.groceries.viewmodel.details.delegate.GroceriesListDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.groceries.viewmodel.details.delegate.IGroceriesListDetailsScreenInputHandler
import com.eyther.lumbridge.features.tools.overview.navigation.ToolsNavigationItem.Groceries.Companion.ARG_GROCERIES_LIST_ID
import com.eyther.lumbridge.model.groceries.GroceriesListEntryUi
import com.eyther.lumbridge.ui.common.composables.model.input.CheckboxInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState
import com.eyther.lumbridge.usecase.groceries.GetGroceriesListUseCase
import com.eyther.lumbridge.usecase.groceries.SaveGroceriesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class GroceriesListDetailsScreenViewModel @Inject constructor(
    private val getGroceriesListUseCase: GetGroceriesListUseCase,
    private val saveGroceriesListUseCase: SaveGroceriesListUseCase,
    private val groceriesListDetailsScreenInputHandler: GroceriesListDetailsScreenInputHandler,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    IGroceriesListDetailsScreenViewModel,
    IGroceriesListDetailsScreenInputHandler by groceriesListDetailsScreenInputHandler {

    companion object {
        private const val TAG = "GroceriesListDetailsScreenViewModel"
    }

    override val viewState: MutableStateFlow<GroceriesListDetailsScreenViewState> =
        MutableStateFlow(GroceriesListDetailsScreenViewState.Loading)

    private val groceriesListId = requireNotNull(savedStateHandle.get<Long>(ARG_GROCERIES_LIST_ID)) {
        "Groceries list ID must be provided or defaulted to -1"
    }

    init {
        viewModelScope.launch {
            inputState
                .onEach { newInputState ->
                    viewState.update {
                        GroceriesListDetailsScreenViewState.Content(
                            inputState = newInputState
                        )
                    }
                }
                .launchIn(this)

            if (groceriesListId != -1L) {
                loadGroceriesList(groceriesListId)
            } else {
                updateInput {
                    GroceriesListDetailsScreenInputState(
                        title = TextInputState(),
                        items = listOf(
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Bananas")
                            ),
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Apples")
                            ),
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Cheese")
                            ),
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Butter")
                            ),
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Bread")
                            ),
                            StableGroceriesListItem(
                                id = UUID.randomUUID(),
                                checkboxState = CheckboxInputState(checked = false),
                                textInputState = TextInputState("Eggs")
                            ),
                        ),
                    )
                }
            }
        }
    }

    private fun loadGroceriesList(groceriesListId: Long) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "Error loading groceries list", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val groceriesList = getGroceriesListUseCase(groceriesListId)

            if (groceriesList == null) {
                viewState.update { GroceriesListDetailsScreenViewState.Content() }
                return@launch
            }

            updateInput {
                it.copy(
                    title = it.title.copy(text = groceriesList.title),
                    items = groceriesList.entries.map { groceriesListEntry ->
                        StableGroceriesListItem(
                            id = UUID.randomUUID(),
                            checkboxState = CheckboxInputState(checked = groceriesListEntry.selected),
                            textInputState = TextInputState(groceriesListEntry.text)
                        )
                    }
                )
            }
        }
    }

    override fun saveGroceriesList() {

    }
}