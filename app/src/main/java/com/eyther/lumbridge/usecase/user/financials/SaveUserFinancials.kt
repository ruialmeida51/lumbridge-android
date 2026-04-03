package com.eyther.lumbridge.usecase.user.financials

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain
import com.eyther.lumbridge.domain.repository.user.UserRepository
import com.eyther.lumbridge.usecase.snapshotsalary.SaveSnapshotNetSalaryUseCase
import javax.inject.Inject

class SaveUserFinancials @Inject constructor(
    private val userRepository: UserRepository,
    private val saveSnapshotNetSalaryUseCase: SaveSnapshotNetSalaryUseCase
) {

    /**
     * Attempts to save the user financial profile.
     *
     * @param userFinancialsDomain the user financials to save.
     */
    suspend operator fun invoke(userFinancialsDomain: UserFinancialsDomain) {
        userRepository.saveUserFinancials(userFinancialsDomain)
        saveSnapshotNetSalaryUseCase(userFinancialsDomain)
    }
}
