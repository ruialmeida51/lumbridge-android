package com.eyther.lumbridge.domain.repository.finance.portugal.irs.model

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain

sealed interface PortugalIrsBracketType {
    companion object {
        const val SOCIAL_SECURITY_RATE = 0.11f

        fun of(userFinancialsDomain: UserFinancialsDomain) = getIrsBracket(userFinancialsDomain)

        private fun getIrsBracket(
            userFinancialsDomain: UserFinancialsDomain
        ): PortugalIrsBracketType {
            return when {
                NoHandicap.FirstTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.FirstTable
                NoHandicap.SecondTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.SecondTable
                NoHandicap.ThirdTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.ThirdTable
                NoHandicap.FourthTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.FourthTable
                Handicapped.FifthTable.isIrsBracket(userFinancialsDomain) -> Handicapped.FifthTable
                Handicapped.SixthTable.isIrsBracket(userFinancialsDomain) -> Handicapped.SixthTable
                Handicapped.SeventhTable.isIrsBracket(userFinancialsDomain) -> Handicapped.SeventhTable
                else -> throw IllegalArgumentException("No IRS bracket found for user financials")
            }
        }
    }

    abstract fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket
    abstract fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean

    sealed interface NoHandicap : PortugalIrsBracketType {
        // Not married with no dependants OR married with two holders and zero or more dependants
        data object FirstTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 820.00 -> 0f
                    monthlySalary > 820.00 && monthlySalary <= 935.00 -> 0.1325f
                    monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.18f
                    monthlySalary > 1001.00 && monthlySalary <= 1123.00 -> 0.18f
                    monthlySalary > 1123.00 && monthlySalary <= 1765.00 -> 0.26f
                    monthlySalary > 1765.00 && monthlySalary <= 2057.00 -> 0.3275f
                    monthlySalary > 2057.00 && monthlySalary <= 2664.00 -> 0.37f
                    monthlySalary > 2664.00 && monthlySalary <= 3193.00 -> 0.3872f
                    monthlySalary > 3193.00 && monthlySalary <= 4173.00 -> 0.4005f
                    monthlySalary > 4173.00 && monthlySalary <= 5470.00 -> 0.4100f
                    monthlySalary > 5470.00 && monthlySalary <= 6450.00 -> 0.4270f
                    monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 0.4717f
                    monthlySalary > 20067.00 -> 0.059f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 820.00 -> 0f
                    monthlySalary > 820.00 && monthlySalary <= 935.00 -> 0.1325f * 2.6f * (1135.39f - monthlySalary)
                    monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.18f * 1.4f * (1385.20f - monthlySalary)
                    monthlySalary > 1001.00 && monthlySalary <= 1123.00 -> 96.82f
                    monthlySalary > 1123.00 && monthlySalary <= 1765.00 -> 186.66f
                    monthlySalary > 1765.00 && monthlySalary <= 2057.00 -> 305.80f
                    monthlySalary > 2057.00 && monthlySalary <= 2664.00 -> 393.23f
                    monthlySalary > 2664.00 && monthlySalary <= 3193.00 -> 439.05f
                    monthlySalary > 3193.00 && monthlySalary <= 4173.00 -> 481.52f
                    monthlySalary > 4173.00 && monthlySalary <= 5470.00 -> 521.17f
                    monthlySalary > 5470.00 && monthlySalary <= 6450.00 -> 614.16f
                    monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 751.31f
                    monthlySalary > 20067.00 -> 1206.80f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val dependantsFlatRate = when {
                    monthlySalary <= 820.00 -> 0f
                    monthlySalary > 820.00 -> 21.43f * (userFinancialsDomain.numberOfDependants?.toFloat() ?: 0f)
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val irsDeduction = (monthlySalary * taxPercentage) - (flatRate + dependantsFlatRate)
                val ssDeduction = monthlySalary * SOCIAL_SECURITY_RATE
                val netSalary = monthlySalary - irsDeduction - ssDeduction

                return PortugalIrsBracket(
                    irsDeductionValue = irsDeduction,
                    irsBracketPercentage = taxPercentage,
                    ssDeductionValue = ssDeduction,
                    ssDeductionPercentage = SOCIAL_SECURITY_RATE,
                    flatRate = flatRate - dependantsFlatRate,
                    netSalary = netSalary
                )
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return (
                        !userFinancialsDomain.married &&
                                userFinancialsDomain.numberOfDependants == 0 &&
                                !userFinancialsDomain.handicapped &&
                                !userFinancialsDomain.irsWithPartner
                        ) || (
                        userFinancialsDomain.married &&
                                !userFinancialsDomain.handicapped &&
                                userFinancialsDomain.irsWithPartner
                        )
            }
        }

        // Not married with one or more dependants
        data object SecondTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return !userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        !userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.irsWithPartner
            }
        }

        // Married single holder with or without dependants
        data object ThirdTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                        !userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.irsWithPartner
            }
        }

        // Handicapped: Not married OR married two holders, no dependants
        data object FourthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return (!userFinancialsDomain.married || userFinancialsDomain.irsWithPartner) &&
                        userFinancialsDomain.numberOfDependants == 0 &&
                        userFinancialsDomain.handicapped
            }
        }
    }

    // Handicapped: Not married, with one or more dependants
    sealed interface Handicapped : PortugalIrsBracketType {
        data object FifthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return !userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.irsWithPartner
            }
        }

        // Handicapped: Married two holders, with dependants
        data object SixthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        userFinancialsDomain.handicapped &&
                        userFinancialsDomain.irsWithPartner
            }
        }

        // Handicapped: Married, single holder, with or without dependants
        data object SeventhTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                TODO("Not yet implemented")
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                        userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.irsWithPartner
            }
        }
    }

}