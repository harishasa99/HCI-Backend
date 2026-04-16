package com.Haris.BudgetTracker.controller

import com.Haris.BudgetTracker.dto.*
import com.Haris.BudgetTracker.repository.UserRepository
import com.Haris.BudgetTracker.security.SecurityUtils
import com.Haris.BudgetTracker.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService,
    private val userRepository: UserRepository
) {

    private fun getCurrentUserId(): Long {
        val email = SecurityUtils.getCurrentUserEmail()
        return userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("Authenticated user not found") }
            .userId
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTransaction(@Valid @RequestBody request: CreateTransactionRequest): TransactionResponse {
        val userId = getCurrentUserId()
        val safeRequest = request.copy(userId = userId)
        return transactionService.createTransaction(safeRequest)
    }

    @GetMapping("/me")
    fun getMyTransactions(): List<TransactionResponse> {
        return transactionService.getAllUserTransactions(getCurrentUserId())
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

    @GetMapping("/me/total-balance")
    fun getTotalBalance(): TotalBalanceResponse {
        return transactionService.getTotalBalance(getCurrentUserId())
    }

    @GetMapping("/me/monthly-report")
    fun getMonthlyReport(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): MonthlyReportResponse {
        return transactionService.getMonthlyReport(getCurrentUserId(), month, year)
    }

    @GetMapping("/me/filter")
    fun getFilteredTransactions(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): List<TransactionResponse> {
        return transactionService.getFilteredTransactions(getCurrentUserId(), month, year)
    }

    @GetMapping("/me/category-report")
    fun getCategoryReport(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): List<CategoryReportResponse> {
        return transactionService.getCategoryReport(getCurrentUserId(), month, year)
    }

    @GetMapping("/me/dashboard")
    fun getDashboard(
        @RequestParam month: Int,
        @RequestParam year: Int
    ): DashboardResponse {
        return transactionService.getDashboard(getCurrentUserId(), month, year)
    }
}