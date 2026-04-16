package com.Haris.BudgetTracker.security

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getCurrentUserEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalStateException("No authentication found")

        return authentication.name
    }

}