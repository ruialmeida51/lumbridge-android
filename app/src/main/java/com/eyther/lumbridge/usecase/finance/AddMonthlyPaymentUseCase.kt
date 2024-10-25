package com.eyther.lumbridge.usecase.finance

import com.eyther.lumbridge.domain.repository.user.UserRepository
import javax.inject.Inject

class AddMonthlyPaymentUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Attempts to add a monthly payment to the user's mortgage. This is done by
     * subtracting the monthly payment capital from the loan amount and incrementing
     * the start date by one month.
     * 
     * @param mortgageUi The mortgage UI to add the amortization to.
     */
    suspend operator fun invoke() {
//        val currentUser = userRepository.getUserMortgage() ?: return
//        val newAmount = mortgageUi.loanAmount - mortgageUi.monthlyPaymentCapital
//
//        userRepository.saveUserMortgage(
//            currentUser.copy(
//                loanAmount = newAmount,
//                startDate = currentUser.startDate.plusMonths(1)
//            )
//        )
    }
}
