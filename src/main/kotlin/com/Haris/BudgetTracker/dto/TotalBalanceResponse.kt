package com.Haris.BudgetTracker.dto

import java.math.BigDecimal

data class TotalBalanceResponse(
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val totalBalance: BigDecimal
)