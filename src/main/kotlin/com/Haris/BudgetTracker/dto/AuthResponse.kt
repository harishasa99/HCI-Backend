package com.Haris.BudgetTracker.dto

data class AuthResponse(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String,
    val message: String
)