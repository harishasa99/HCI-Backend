package com.Haris.BudgetTracker.dto

import java.math.BigDecimal

data class MonthlyReportResponse(
    val year: Int,
    val month: Int,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val balance: BigDecimal
)