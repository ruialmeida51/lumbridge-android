package com.eyther.lumbridge.features.editfinancialprofile.viewmodel

interface EditFinancialProfileInputHandler {
    fun onAnnualGrossSalaryChanged(annualGrossSalary: Float?)
    fun onFoodCardPerDiemChanged(foodCardPerDiem: Float?)
    fun onSavingsPercentageChanged(savingsPercentage: Int?)
    fun onNecessitiesPercentageChanged(necessitiesPercentage: Int?)
    fun onLuxuriesPercentageChanged(luxuriesPercentage: Int?)
    fun onNumberOfDependantsChanged(numberOfDependants: Int?)
    fun onIrsWithPartnerChanged(irsWithPartner: Boolean)
    fun onMarriedChanged(married: Boolean)
    fun onHandicappedChanged(handicapped: Boolean)
}
