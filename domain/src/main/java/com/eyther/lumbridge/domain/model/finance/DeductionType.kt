package com.eyther.lumbridge.domain.model.finance

/**
 * Deduction types may differ between countries. Therefore, there's no reliable way to
 * represent them as a single class. This interface is used to represent the different
 * deduction types that may exist in a country.
 */
sealed class DeductionType(open val name: String) {
    /**
     * These are the deduction types that exist in Portugal.
     */
    sealed class PortugalDeductionType(override val name: String) : DeductionType(name) {
        data object Flat : PortugalDeductionType("Flat")
        data object SocialSecurity : PortugalDeductionType("Social Security")
        data object IRS : PortugalDeductionType("IRS")
    }
}
