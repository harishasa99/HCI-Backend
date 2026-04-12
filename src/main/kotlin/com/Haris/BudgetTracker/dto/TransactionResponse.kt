package com.Haris.BudgetTracker.dto

import com.Haris.BudgetTracker.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import com.Haris.BudgetTracker.model.TransactionCategory

data class TransactionResponse(
    val transactionId: Long,
    val description: String,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val category: TransactionCategory,
    val transactionDate: LocalDate,
    val userId: Long,
    val userName: String
)