package com.Haris.BudgetTracker.repository

import com.Haris.BudgetTracker.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllByUserUserIdOrderByTransactionDateDesc(userId: Long): List<Transaction>
}