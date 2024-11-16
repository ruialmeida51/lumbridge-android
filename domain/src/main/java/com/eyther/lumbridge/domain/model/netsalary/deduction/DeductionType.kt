package com.eyther.lumbridge.domain.model.netsalary.deduction

/**
 * Deduction types may differ between countries. Therefore, there's no reliable way to
 * represent them as a single class. This interface is used to represent the different
 * deduction types that may exist in a country.
 */
sealed interface DeductionType {
    /**
     * These are the deduction types that exist in Portugal.
     */
    sealed class PortugalDeductionType : DeductionType {
        data object SocialSecurity : PortugalDeductionType()
        data object IRS : PortugalDeductionType()
    }
}
