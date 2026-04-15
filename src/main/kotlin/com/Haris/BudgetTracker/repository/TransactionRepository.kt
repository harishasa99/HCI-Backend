package com.Haris.BudgetTracker.repository

import com.Haris.BudgetTracker.model.Transaction
import com.Haris.BudgetTracker.model.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal

interface TransactionRepository : JpaRepository<Transaction, Long> {

    fun findAllByUserUserIdOrderByTransactionDateDesc(userId: Long): List<Transaction>

    @Query(
        """
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.user.userId = :userId
        AND t.transactionType = :type
        """
    )
    fun sumAmountByUserIdAndType(
        @Param("userId") userId: Long,
        @Param("type") type: TransactionType
    ): BigDecimal

    @Query(
        """
        SELECT t
        FROM Transaction t
        WHERE t.user.userId = :userId
        AND YEAR(t.transactionDate) = :year
        AND MONTH(t.transactionDate) = :month
        ORDER BY t.transactionDate DESC, t.transactionId DESC
        """
    )
    fun findAllByUserIdAndMonthAndYear(
        @Param("userId") userId: Long,
        @Param("month") month: Int,
        @Param("year") year: Int
    ): List<Transaction>

    @Query(
        """
        SELECT t
        FROM Transaction t
        WHERE t.user.userId = :userId
        AND YEAR(t.transactionDate) = :year
        AND MONTH(t.transactionDate) = :month
        ORDER BY t.transactionDate DESC, t.transactionId DESC
        """
    )
    fun findFilteredTransactions(
        @Param("userId") userId: Long,
        @Param("month") month: Int,
        @Param("year") year: Int
    ): List<Transaction>

    @Query(
        """
    SELECT t.category, COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.user.userId = :userId
    AND t.transactionType = com.Haris.BudgetTracker.model.TransactionType.EXPENSE
    AND YEAR(t.transactionDate) = :year
    AND MONTH(t.transactionDate) = :month
    GROUP BY t.category
    ORDER BY SUM(t.amount) DESC
    """
    )
    fun getCategoryReport(
        @Param("userId") userId: Long,
        @Param("month") month: Int,
        @Param("year") year: Int
    ): List<Array<Any>>

    fun countByUserUserId(userId: Long): Int

    @Query(
        """
    SELECT COUNT(t)
    FROM Transaction t
    WHERE t.user.userId = :userId
    AND YEAR(t.transactionDate) = :year
    AND MONTH(t.transactionDate) = :month
    """
    )
    fun countByUserIdAndMonthAndYear(
        @Param("userId") userId: Long,
        @Param("month") month: Int,
        @Param("year") year: Int
    ): Int
}