package com.Haris.Budgettracker.controller

import com.Haris.BudgetTracker.dto.CreateTransactionRequest
import com.Haris.BudgetTracker.dto.TransactionResponse
import com.Haris.BudgetTracker.model.Transaction
import com.Haris.BudgetTracker.service.TransactionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    fun createTransaction(@Valid @RequestBody request: CreateTransactionRequest): Transaction {
        return transactionService.createTransaction(request)
    }

    @GetMapping("/user/{userId}")
    fun getUserTransactions(@PathVariable userId: Long): List<TransactionResponse> {
        return transactionService.getAllUserTransactions(userId)
    }
}