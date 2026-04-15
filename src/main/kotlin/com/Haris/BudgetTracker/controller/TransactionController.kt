package com.Haris.BudgetTracker.controller

import com.Haris.BudgetTracker.dto.CreateTransactionRequest
import com.Haris.BudgetTracker.dto.MonthlyReportResponse
import com.Haris.BudgetTracker.dto.TotalBalanceResponse
import com.Haris.BudgetTracker.dto.TransactionResponse
import com.Haris.BudgetTracker.dto.UpdateTransactionRequest
import com.Haris.BudgetTracker.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import com.Haris.BudgetTracker.dto.CategoryReportResponse
import com.Haris.BudgetTracker.dto.DashboardResponse

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransaction(@Valid @RequestBody request: CreateTransactionRequest): TransactionResponse {
        return transactionService.createTransaction(request)
    }

    @GetMapping("/user/{userId}")
    fun getUserTransactions(@PathVariable userId: Long): List<TransactionResponse> {
        return transactionService.getAllUserTransactions(userId)
    }

    @PutMapping("/{transactionId}")
    fun updateTransaction(
        @PathVariable transactionId: Long,
        @Valid @RequestBody request: UpdateTransactionRequest
    ): TransactionResponse {
        return transactionService.updateTransaction(transactionId, request)
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTransaction(@PathVariable transactionId: Long) {
        transactionService.deleteTransaction(transactionId)
    }

    @GetMapping("/user/{userId}/total-balance")
    fun getTotalBalance(@PathVariable userId: Long): TotalBalanceResponse {
        return transactionService.getTotalBalance(userId)
    }

    @GetMapping("/user/{userId}/monthly-report")
    fun getMonthlyReport(
        @PathVariable userId: Long,
        @RequestParam month: Int,
        @RequestParam year: Int
    ): MonthlyReportResponse {
        return transactionService.getMonthlyReport(userId, month, year)
    }

    @GetMapping("/user/{userId}/filter")
    fun getFilteredTransactions(
        @PathVariable userId: Long,
        @RequestParam month: Int,
        @RequestParam year: Int
    ): List<TransactionResponse> {
        return transactionService.getFilteredTransactions(userId, month, year)
    }

    @GetMapping("/user/{userId}/category-report")
    fun getCategoryReport(
        @PathVariable userId: Long,
        @RequestParam month: Int,
        @RequestParam year: Int
    ): List<CategoryReportResponse> {
        return transactionService.getCategoryReport(userId, month, year)
    }

    @GetMapping("/user/{userId}/dashboard")
    fun getDashboard(
        @PathVariable userId: Long,
        @RequestParam month: Int,
        @RequestParam year: Int
    ): DashboardResponse {
        return transactionService.getDashboard(userId, month, year)
    }
}