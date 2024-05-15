package com.eyther.lumbridge.model.mortgage

data class MortgageAmortizationUi(
    val amortization: Float,
    val remainder: Float,
    val nextPayment: Float
) {
    /**
     * The number of data elements in this class.
     * This is used to create a table in the UI, as such we need to know how many elements are
     * in this class.
     *
     * This should be updated if the number of elements in this class changes and we want
     * to display the new elements in the table.
     *
     * @return The number of elements in this class.
     */
    val numberOfElements
        get() = 3
}
