package com.eyther.lumbridge.features.tools.savings.model

sealed interface SavingsScreenViewState {
    // The initial state of the screen. The screen is loading.
    data object Loading : SavingsScreenViewState

    // There's no data available to calculate savings. Refer to net salary calculator.
    data object RequiresInput : SavingsScreenViewState

    // The screen is ready to display the savings calculator.
    sealed class Content(
        open val savingsPercentage: Float? = null,
        open val necessitiesPercentage: Float? = null,
        open val wantsPercentage: Float? = null
    ) : SavingsScreenViewState {

        // Input mode - we need to ask the user what percentages should be allocated to each
        // category.
        data class InputPercentages(
            override val savingsPercentage: Float?,
            override val necessitiesPercentage: Float?,
            override val wantsPercentage: Float?
        ) : Content()

        // Overview mode - the user was already asked for the percentages and we have the data to
        // display.
        data class Overview(
            override val savingsPercentage: Float,
            override val necessitiesPercentage: Float,
            override val wantsPercentage: Float
        ) : Content()
    }
}
