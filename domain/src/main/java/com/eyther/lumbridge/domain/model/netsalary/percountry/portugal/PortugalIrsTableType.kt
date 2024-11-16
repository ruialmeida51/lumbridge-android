package com.eyther.lumbridge.domain.model.netsalary.percountry.portugal

import com.eyther.lumbridge.domain.model.user.UserFinancialsDomain

sealed interface PortugalIrsTableType {
    companion object {
        /**
         * Returns the IRS bracket for the user financials provided.
         *
         * @param userFinancialsDomain the user financials to calculate the IRS bracket from
         *
         * @return the IRS bracket
         */
        fun of(
            userFinancialsDomain: UserFinancialsDomain
        ) = when {
            NoHandicap.FirstTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.FirstTable
            NoHandicap.SecondTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.SecondTable
            NoHandicap.ThirdTable.isIrsBracket(userFinancialsDomain) -> NoHandicap.ThirdTable
            Handicapped.FourthTable.isIrsBracket(userFinancialsDomain) -> Handicapped.FourthTable
            Handicapped.FifthTable.isIrsBracket(userFinancialsDomain) -> Handicapped.FifthTable
            Handicapped.SixthTable.isIrsBracket(userFinancialsDomain) -> Handicapped.SixthTable
            Handicapped.SeventhTable.isIrsBracket(userFinancialsDomain) -> Handicapped.SeventhTable
            else -> throw IllegalArgumentException("No IRS bracket found for user financials")
        }
    }

    /**
     * Returns whether the user financials provided match the IRS bracket.
     * @param userFinancialsDomain the user financials to check if they match the IRS bracket
     * @return true if the user financials match the IRS bracket, false otherwise
     * @throws IllegalArgumentException if no IRS bracket is found for the user financials
     */
    fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean

    sealed interface NoHandicap : PortugalIrsTableType {

        // Not married with no dependants OR married with two holders and zero or more dependants
        data object FirstTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return (
                    !userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) == 0 &&
                        !userFinancialsDomain.handicapped
                    ) || (
                    userFinancialsDomain.married &&
                        !userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.singleIncome
                    )
            }
        }

        // Not married with one or more dependants
        data object SecondTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return !userFinancialsDomain.married &&
                    (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                    !userFinancialsDomain.handicapped
            }
        }

        // Married single holder with or without dependants
        data object ThirdTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                    !userFinancialsDomain.handicapped &&
                    userFinancialsDomain.singleIncome
            }
        }
    }

    sealed interface Handicapped : PortugalIrsTableType {

        // Handicapped: Not married OR married two holders, no dependants
        data object FourthTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return (!userFinancialsDomain.married || !userFinancialsDomain.singleIncome) &&
                    (userFinancialsDomain.numberOfDependants ?: 0) == 0 &&
                    userFinancialsDomain.handicapped
            }
        }

        // Handicapped: Not married, with one or more dependants
        data object FifthTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return !userFinancialsDomain.married &&
                    (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                    userFinancialsDomain.handicapped
            }
        }

        // Handicapped: Married two holders, with dependants
        data object SixthTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                    (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                    userFinancialsDomain.handicapped &&
                    !userFinancialsDomain.singleIncome
            }
        }

        // Handicapped: Married, single holder, with or without dependants
        data object SeventhTable : PortugalIrsTableType {
            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return userFinancialsDomain.married &&
                    userFinancialsDomain.handicapped &&
                    userFinancialsDomain.singleIncome
            }
        }
    }
}
