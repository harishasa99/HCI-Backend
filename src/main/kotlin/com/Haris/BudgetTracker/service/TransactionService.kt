package com.Haris.BudgetTracker.service

import com.Haris.BudgetTracker.dto.CreateTransactionRequest
import com.Haris.BudgetTracker.dto.MonthlyReportResponse
import com.Haris.BudgetTracker.dto.TotalBalanceResponse
import com.Haris.BudgetTracker.dto.TransactionResponse
import com.Haris.BudgetTracker.dto.UpdateTransactionRequest
import com.Haris.BudgetTracker.exception.ResourceNotFoundException
import com.Haris.BudgetTracker.model.Transaction
import com.Haris.BudgetTracker.model.TransactionType
import com.Haris.BudgetTracker.repository.TransactionRepository
import com.Haris.BudgetTracker.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import com.Haris.BudgetTracker.dto.CategoryReportResponse
import com.Haris.BudgetTracker.model.TransactionCategory
import com.Haris.BudgetTracker.dto.DashboardResponse

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {

    fun createTransaction(request: CreateTransactionRequest): TransactionResponse {
        val user = userRepository.findById(request.userId)
            .orElseThrow { ResourceNotFoundException("User with id ${request.userId} not found") }

        val transaction = Transaction(
            description = request.description,
            amount = request.amount,
            transactionType = request.transactionType,
            category = request.category,
            transactionDate = request.transactionDate,
            user = user
        )

        val savedTransaction = transactionRepository.save(transaction)
        return mapToTransactionResponse(savedTransaction)
    }

    fun getAllUserTransactions(userId: Long): List<TransactionResponse> {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        return transactionRepository
            .findAllByUserUserIdOrderByTransactionDateDesc(userId)
            .map { mapToTransactionResponse(it) }
    }

    fun updateTransaction(transactionId: Long, request: UpdateTransactionRequest): TransactionResponse {
        val existingTransaction = transactionRepository.findById(transactionId)
            .orElseThrow { ResourceNotFoundException("Transaction with id $transactionId not found") }

        val updatedTransaction = Transaction(
            transactionId = existingTransaction.transactionId,
            description = request.description,
            amount = request.amount,
            transactionType = request.transactionType,
            category = request.category,
            transactionDate = request.transactionDate,
            createdAt = existingTransaction.createdAt,
            user = existingTransaction.user
        )

        val savedTransaction = transactionRepository.save(updatedTransaction)
        return mapToTransactionResponse(savedTransaction)
    }

    fun deleteTransaction(transactionId: Long) {
        val transaction = transactionRepository.findById(transactionId)
            .orElseThrow { ResourceNotFoundException("Transaction with id $transactionId not found") }

        transactionRepository.delete(transaction)
    }

    fun getTotalBalance(userId: Long): TotalBalanceResponse {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        val totalIncome = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.INCOME)
        val totalExpense = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE)
        val totalBalance = totalIncome.subtract(totalExpense)

        return TotalBalanceResponse(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            totalBalance = totalBalance
        )
    }

    fun getMonthlyReport(userId: Long, month: Int, year: Int): MonthlyReportResponse {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        val transactions = transactionRepository.findAllByUserIdAndMonthAndYear(userId, month, year)

        val totalIncome = transactions
            .filter { it.transactionType == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }

        val totalExpense = transactions
            .filter { it.transactionType == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }

        val balance = totalIncome.subtract(totalExpense)

        return MonthlyReportResponse(
            year = year,
            month = month,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance
        )
    }

    private fun mapToTransactionResponse(transaction: Transaction): TransactionResponse {
        return TransactionResponse(
            transactionId = transaction.transactionId,
            description = transaction.description,
            amount = transaction.amount,
            transactionType = transaction.transactionType,
            category = transaction.category,
            transactionDate = transaction.transactionDate,
            userId = transaction.user.userId,
            userName = transaction.user.name
        )

    }
    fun getFilteredTransactions(userId: Long, month: Int, year: Int): List<TransactionResponse> {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        return transactionRepository.findFilteredTransactions(userId, month, year)
            .map { mapToTransactionResponse(it) }
    }

    fun getCategoryReport(userId: Long, month: Int, year: Int): List<CategoryReportResponse> {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        return transactionRepository.getCategoryReport(userId, month, year).map { row ->
            CategoryReportResponse(
                category = row[0] as TransactionCategory,
                totalAmount = row[1] as BigDecimal
            )
        }
    }

    fun getDashboard(userId: Long, month: Int, year: Int): DashboardResponse {
        if (!userRepository.existsById(userId)) {
            throw ResourceNotFoundException("User with id $userId not found")
        }

        val totalIncome = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.INCOME)
        val totalExpense = transactionRepository.sumAmountByUserIdAndType(userId, TransactionType.EXPENSE)
        val totalBalance = totalIncome.subtract(totalExpense)

        val monthlyTransactions = transactionRepository.findAllByUserIdAndMonthAndYear(userId, month, year)

        val monthlyIncome = monthlyTransactions
            .filter { it.transactionType == TransactionType.INCOME }
            .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }

        val monthlyExpense = monthlyTransactions
            .filter { it.transactionType == TransactionType.EXPENSE }
            .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }

        val monthlyBalance = monthlyIncome.subtract(monthlyExpense)

        val totalTransactionCount = transactionRepository.countByUserUserId(userId)
        val monthlyTransactionCount = transactionRepository.countByUserIdAndMonthAndYear(userId, month, year)

        return DashboardResponse(
            userId = userId,
            year = year,
            month = month,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            totalBalance = totalBalance,
            monthlyIncome = monthlyIncome,
            monthlyExpense = monthlyExpense,
            monthlyBalance = monthlyBalance,
            totalTransactionCount = totalTransactionCount,
            monthlyTransactionCount = monthlyTransactionCount
        )
    }



}