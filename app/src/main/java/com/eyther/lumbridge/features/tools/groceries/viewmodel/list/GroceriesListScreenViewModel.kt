package com.eyther.lumbridge.features.tools.groceries.viewmodel.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.groceries.model.list.GroceriesListScreenViewState
import com.eyther.lumbridge.model.groceries.GroceriesListUi
import com.eyther.lumbridge.usecase.groceries.DeleteGroceriesListUseCase
import com.eyther.lumbridge.usecase.groceries.GetAllGroceriesListFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceriesListScreenViewModel @Inject constructor(
    private val getAllGroceriesListFlowUseCase: GetAllGroceriesListFlowUseCase,
    private val deleteGroceriesListUseCase: DeleteGroceriesListUseCase
) : ViewModel(),
    IGroceriesListScreenViewModel {

    companion object {
        private const val TAG = "GroceriesListsScreenViewModel"
    }

    override val viewState: MutableStateFlow<GroceriesListScreenViewState> =
        MutableStateFlow(GroceriesListScreenViewState.Loading)

    init {
        fetchGroceriesLists()
    }

    private fun fetchGroceriesLists() {
        viewModelScope.launch {
            val groceriesListFlow = getAllGroceriesListFlowUseCase()

            groceriesListFlow
                .onEach { groceriesLists ->
                    if (groceriesLists.isEmpty()) {
                        viewState.update { GroceriesListScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        GroceriesListScreenViewState.Content(
                            groceriesList = groceriesLists.map { list ->
                                list.copy(
                                    entries = list.entries.filter { entry ->
                                        if (list.showTickedItems) true
                                        else !entry.selected
                                    }
                                )
                            }
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteGroceryItem(groceryItem: GroceriesListUi) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "💥 Error deleting grocery item $groceryItem", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteGroceriesListUseCase(groceryItem.id)
        }
    }
}
