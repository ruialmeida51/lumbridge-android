package com.eyther.lumbridge.features.tools.netsalary.viewmodel.input

import androidx.navigation.NavHostController
import com.eyther.lumbridge.domain.model.locale.SupportedLocales
import com.eyther.lumbridge.features.tools.netsalary.model.input.NetSalaryInputScreenViewState
import com.eyther.lumbridge.features.tools.netsalary.viewmodel.input.delegate.INetSalaryInputScreenInputHandler
import com.eyther.lumbridge.model.finance.NetSalaryUi
import kotlinx.coroutines.flow.StateFlow

interface INetSalaryInputScreenViewModel : INetSalaryInputScreenInputHandler {
    val viewState: StateFlow<NetSalaryInputScreenViewState>

    /**
     * Calculate the net salary based on the user's input. This function will only store
     * the data in memory and will not persist it to the database. To do that, the user
     * must use the financial profile on the profile screen.
     *
     * @param navController The navigation controller to use to navigate to the result screen
     * @param cacheArguments A function that will cache the arguments for the result screen in a
     * shared cache
     */
    fun onCalculateNetSalary(
        navController: NavHostController,
        cacheArguments: (
            netSalaryUi: NetSalaryUi,
            locale: SupportedLocales
        ) -> Unit
    )

    /**
     * When something goes wrong, the user can retry to input the data and see if that
     * fixes the issue. This function will reset the input fields to their default values.
     */
    fun onRetryInput()
}
