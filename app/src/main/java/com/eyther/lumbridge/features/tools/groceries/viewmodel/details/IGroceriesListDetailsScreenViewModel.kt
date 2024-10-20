package com.eyther.lumbridge.features.tools.groceries.viewmodel.details

import com.eyther.lumbridge.features.tools.groceries.model.details.GroceriesListDetailsScreenViewState
import com.eyther.lumbridge.features.tools.groceries.viewmodel.details.delegate.IGroceriesListDetailsScreenInputHandler
import kotlinx.coroutines.flow.StateFlow

interface IGroceriesListDetailsScreenViewModel : IGroceriesListDetailsScreenInputHandler {
    val viewState: StateFlow<GroceriesListDetailsScreenViewState>

    /**
     * Saves the groceries list to the database. This will be called on onPause and on onResume. Iif the user leaves the screen,
     * we save the list.
     *
     * The idea here is to check if there is any content to save and only then insert it into the database. The user
     * might've just opened the screen and closed it without adding anything.
     */
    fun saveGroceriesList()
}