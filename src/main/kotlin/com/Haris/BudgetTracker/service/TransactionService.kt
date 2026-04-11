package com.Haris.BudgetTracker.service

import com.Haris.BudgetTracker.dto.CreateTransactionRequest
import com.Haris.BudgetTracker.model.Transaction
import com.Haris.BudgetTracker.repository.TransactionRepository
import com.Haris.BudgetTracker.repository.UserRepository
import org.springframework.stereotype.Service
import com.Haris.BudgetTracker.dto.TransactionResponse

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {

    fun createTransaction(request: CreateTransactionRequest): Transaction {
        val user = userRepository.findById(request.userId)
            .orElseThrow { RuntimeException("User not found") }

        val transaction = Transaction(
            description = request.description,
            amount = request.amount,
            transactionType = request.transactionType,
            transactionDate = request.transactionDate,
            user = user
        )

        return transactionRepository.save(transaction)
    }

    fun getAllUserTransactions(userId: Long): List<TransactionResponse> {
        return transactionRepository
            .findAllByUserUserIdOrderByTransactionDateDesc(userId)
            .map {
                TransactionResponse(
                    transactionId = it.transactionId,
                    description = it.description,
                    amount = it.amount,
                    transactionType = it.transactionType,
                    transactionDate = it.transactionDate,
                    userId = it.user.userId,
                    userName = it.user.name
                )
            }
    }


}