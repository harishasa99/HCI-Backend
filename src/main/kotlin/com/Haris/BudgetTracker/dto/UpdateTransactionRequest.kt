package com.Haris.BudgetTracker.dto

import com.Haris.BudgetTracker.model.TransactionType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import com.Haris.BudgetTracker.model.TransactionCategory

data class UpdateTransactionRequest(

    @field:NotBlank
    val description: String,

    @field:NotNull
    @field:DecimalMin("0.01")
    val amount: BigDecimal,

    @field:NotNull
    val transactionType: TransactionType,

    @field:NotNull
    val category: TransactionCategory,

    @field:NotNull
    val transactionDate: LocalDate
)