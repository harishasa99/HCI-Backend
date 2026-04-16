package com.Haris.BudgetTracker.controller

import com.Haris.BudgetTracker.dto.AuthResponse
import com.Haris.BudgetTracker.dto.LoginRequest
import com.Haris.BudgetTracker.dto.RegisterRequest
import com.Haris.BudgetTracker.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterRequest): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse {
        return authService.login(request)
    }
}