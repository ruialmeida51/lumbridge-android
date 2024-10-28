package com.eyther.lumbridge.usecase.finance

import java.time.LocalDate
import java.time.Month
import java.time.Year
import javax.inject.Inject

class GetSnapshotSalaryUseCase @Inject constructor() {
    suspend operator fun invoke(year: Year, month: Month): Float {
        return 1500f
    }
}