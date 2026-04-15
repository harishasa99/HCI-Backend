package com.Haris.BudgetTracker.dto

import java.math.BigDecimal

data class DashboardResponse(
    val userId: Long,
    val year: Int,
    val month: Int,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val totalBalance: BigDecimal,
    val monthlyIncome: BigDecimal,
    val monthlyExpense: BigDecimal,
    val monthlyBalance: BigDecimal,
    val totalTransactionCount: Int,
    val monthlyTransactionCount: Int
)