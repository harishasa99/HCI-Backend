package com.Haris.BudgetTracker.dto

import com.Haris.BudgetTracker.model.TransactionCategory
import java.math.BigDecimal

data class CategoryReportResponse(
    val category: TransactionCategory,
    val totalAmount: BigDecimal
)