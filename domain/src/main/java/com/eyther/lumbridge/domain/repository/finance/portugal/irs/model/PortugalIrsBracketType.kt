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
                    monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 0.4495f
                    monthlySalary > 20067.00 -> 0.4717f
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
                    monthlySalary > 820.00 -> 21.43f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
        data object SecondTable : PortugalIrsBracketType {
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
                    monthlySalary > 6450.00 && monthlySalary <= 20067.00 -> 0.4495f
                    monthlySalary > 20067.00 -> 0.4717f
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
                    monthlySalary > 820.00 -> 34.29f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
                return !userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        !userFinancialsDomain.handicapped
            }
        }

        // Married single holder with or without dependants
        data object ThirdTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 857.00 -> 0f
                    monthlySalary > 857.00 && monthlySalary <= 935.00 -> 0.1325f
                    monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.1325f
                    monthlySalary > 1001.00 && monthlySalary <= 1393.00 -> 0.1325f
                    monthlySalary > 1393.00 && monthlySalary <= 1900.00 -> 0.1850f
                    monthlySalary > 1900.00 && monthlySalary <= 2801.00 -> 0.26f
                    monthlySalary > 2801.00 && monthlySalary <= 3423.00 -> 0.28f
                    monthlySalary > 3423.00 && monthlySalary <= 4099.00 -> 0.2915f
                    monthlySalary > 4099.00 && monthlySalary <= 5800.00 -> 0.3250f
                    monthlySalary > 5800.00 && monthlySalary <= 6422.00 -> 0.36f
                    monthlySalary > 6422.00 && monthlySalary < 20064.21 -> 0.4250f
                    monthlySalary >= 20064.21 -> 0.4717f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 857.00 -> 0f
                    monthlySalary > 857.00 && monthlySalary <= 935.00 -> 0.1325f * 2.6f * (1182.62f - monthlySalary)
                    monthlySalary > 935.00 && monthlySalary <= 1001.00 -> 0.18f * 1.4f * (1402.30f - monthlySalary)
                    monthlySalary > 1001.00 && monthlySalary <= 1393.00 -> 74.44f
                    monthlySalary > 1393.00 && monthlySalary <= 1900.00 -> 147.57f
                    monthlySalary > 1900.00 && monthlySalary <= 2801.00 -> 290.07f
                    monthlySalary > 2801.00 && monthlySalary <= 3423.00 -> 346.09f
                    monthlySalary > 3423.00 && monthlySalary <= 4099.00 -> 385.46f
                    monthlySalary > 4099.00 && monthlySalary <= 5800.00 -> 522.78f
                    monthlySalary > 5800.00 && monthlySalary <= 6422.00 -> 725.78f
                    monthlySalary > 6422.00 && monthlySalary < 20064.21 -> 1143.21f
                    monthlySalary >= 20064.21 -> 2080.20f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val dependantsFlatRate = when {
                    monthlySalary <= 857.00 -> 0f
                    monthlySalary > 857.00 -> 42.86f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
                return userFinancialsDomain.married &&
                        !userFinancialsDomain.handicapped &&
                        userFinancialsDomain.singleIncome
            }
        }

        // Handicapped: Not married OR married two holders, no dependants
        data object FourthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 1519.41 -> 0f
                    monthlySalary > 1519.41 && monthlySalary <= 1648.29 -> 0.1325f
                    monthlySalary > 1648.29 && monthlySalary <= 1994.61 -> 0.23f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 0.3275f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 0.37f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 0.4005f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 0.4228f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 0.4495f
                    monthlySalary >= 20264.85 -> 0.4717f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 1519.41 -> 0f
                    monthlySalary > 1519.41 && monthlySalary <= 1648.29 -> 201.32f
                    monthlySalary > 1648.29 && monthlySalary <= 1994.61 -> 362.03f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 556.51f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 658.97f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 792.37f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 940.03f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 1119.39f
                    monthlySalary >= 20264.85 -> 1569.27f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val irsDeduction = (monthlySalary * taxPercentage) - flatRate
                val ssDeduction = monthlySalary * SOCIAL_SECURITY_RATE
                val netSalary = monthlySalary - irsDeduction - ssDeduction

                return PortugalIrsBracket(
                    irsDeductionValue = irsDeduction,
                    irsBracketPercentage = taxPercentage,
                    ssDeductionValue = ssDeduction,
                    ssDeductionPercentage = SOCIAL_SECURITY_RATE,
                    flatRate = flatRate,
                    netSalary = netSalary
                )
            }

            override fun isIrsBracket(userFinancialsDomain: UserFinancialsDomain): Boolean {
                return (!userFinancialsDomain.married || !userFinancialsDomain.singleIncome) &&
                        (userFinancialsDomain.numberOfDependants ?: 0) == 0 &&
                        userFinancialsDomain.handicapped
            }
        }
    }

    // Handicapped: Not married, with one or more dependants
    sealed interface Handicapped : PortugalIrsBracketType {
        data object FifthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 1677.09 -> 0f
                    monthlySalary > 1677.09 && monthlySalary <= 1994.61 -> 0.23f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 0.3275f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 0.37f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 0.4005f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 0.4228f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 0.4495f
                    monthlySalary >= 20264.85 -> 0.4717f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 1677.09 -> 0f
                    monthlySalary > 1677.09 && monthlySalary <= 1994.61 -> 383.73f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 580.21f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 682.67f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 816.07f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 963.73f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 1143.09f
                    monthlySalary >= 20264.85 -> 1592.97f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val dependantsFlatRate = when {
                    monthlySalary <= 1677.09 -> 0f
                    monthlySalary > 1677.09 -> 42.86f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
                return !userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        userFinancialsDomain.handicapped
            }
        }

        // Handicapped: Married two holders, with dependants
        data object SixthTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 1574.66 -> 0f
                    monthlySalary > 1574.66 && monthlySalary <= 1648.29 -> 0.1325f
                    monthlySalary > 1648.29 && monthlySalary <= 1994.61 -> 0.23f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 0.3275f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 0.37f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 0.4005f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 0.4228f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 0.4495f
                    monthlySalary >= 20264.85 -> 0.4717f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 1574.66 -> 0f
                    monthlySalary > 1574.66 && monthlySalary <= 1648.29 -> 208.64f
                    monthlySalary > 1648.29 && monthlySalary <= 1994.61 -> 369.35f
                    monthlySalary > 1994.61 && monthlySalary <= 2410.71 -> 563.83f
                    monthlySalary > 2410.71 && monthlySalary <= 4373.75 -> 666.29f
                    monthlySalary > 4373.75 && monthlySalary <= 6621.18 -> 799.69f
                    monthlySalary > 6621.18 && monthlySalary <= 6717.41 -> 947.35f
                    monthlySalary > 6717.41 && monthlySalary < 20264.85 -> 1126.71f
                    monthlySalary >= 20264.85 -> 1576.59f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val dependantsFlatRate = when {
                    monthlySalary <= 1574.66 -> 0f
                    monthlySalary > 1574.66 -> 21.43f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
                return userFinancialsDomain.married &&
                        (userFinancialsDomain.numberOfDependants ?: 0) > 0 &&
                        userFinancialsDomain.handicapped &&
                        !userFinancialsDomain.singleIncome
            }
        }

        // Handicapped: Married, single holder, with or without dependants
        data object SeventhTable : PortugalIrsBracketType {
            override fun getIrsBracket(userFinancialsDomain: UserFinancialsDomain): PortugalIrsBracket {
                val monthlySalary = userFinancialsDomain.annualGrossSalary / 14f

                val taxPercentage = when {
                    monthlySalary <= 2105.51 -> 0f
                    monthlySalary > 2105.51 && monthlySalary <= 3622.95 -> 0.3160f
                    monthlySalary > 3622.95 && monthlySalary <= 6587.01 -> 0.33f
                    monthlySalary > 6587.01 && monthlySalary < 20264.85 -> 0.4250f
                    monthlySalary > 20264.85 -> 0.4717f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val flatRate = when {
                    monthlySalary <= 2105.51 -> 0f
                    monthlySalary > 2105.51 && monthlySalary <= 3622.95 -> 719.35f
                    monthlySalary > 3622.95 && monthlySalary <= 6587.01 -> 770.07f
                    monthlySalary > 6587.01 && monthlySalary < 20264.85 -> 1395.84f
                    monthlySalary > 20264.85 -> 2452.21f
                    else -> error("💥 Could not match salary to tax bracket.")
                }

                val dependantsFlatRate = when {
                    monthlySalary <= 2105.51 -> 0f
                    monthlySalary > 2105.51 -> 42.86f * (userFinancialsDomain.numberOfDependants?.toFloat()
                        ?: 0f)

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
                return userFinancialsDomain.married &&
                        userFinancialsDomain.handicapped &&
                        userFinancialsDomain.singleIncome
            }
        }
    }

}