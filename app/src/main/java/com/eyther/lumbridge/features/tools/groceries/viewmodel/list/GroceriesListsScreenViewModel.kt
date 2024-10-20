package com.eyther.lumbridge.features.tools.groceries.viewmodel.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.features.tools.groceries.model.list.GroceriesListsScreenViewState
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
class GroceriesListsScreenViewModel @Inject constructor(
    private val getAllGroceriesListFlowUseCase: GetAllGroceriesListFlowUseCase,
    private val deleteGroceriesListUseCase: DeleteGroceriesListUseCase
) : ViewModel(), IGroceriesListScreenViewModel {
    companion object {
        private const val TAG = "GroceriesListsScreenViewModel"
    }

    override val viewState: MutableStateFlow<GroceriesListsScreenViewState> =
        MutableStateFlow(GroceriesListsScreenViewState.Loading)

    init {
        fetchGroceriesLists()
    }

    private fun fetchGroceriesLists() {
        viewModelScope.launch {
            val groceriesListFlow = getAllGroceriesListFlowUseCase()

            groceriesListFlow
                .onEach { groceriesLists ->
                    if (groceriesLists.isEmpty()) {
                        viewState.update { GroceriesListsScreenViewState.Empty }
                        return@onEach
                    }

                    viewState.update {
                        GroceriesListsScreenViewState.Content(
                            groceriesList = groceriesLists
                        )
                    }
                }
                .launchIn(this)
        }
    }

    override fun onDeleteGroceryItem(groceryItem: GroceriesListUi) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "\uD83D\uDCA5 Error deleting grocery item", throwable)
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            deleteGroceriesListUseCase(groceryItem.id)
        }
    }
}