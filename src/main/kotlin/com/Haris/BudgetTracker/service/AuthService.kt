package com.Haris.BudgetTracker.service

import com.Haris.BudgetTracker.dto.AuthResponse
import com.Haris.BudgetTracker.dto.LoginRequest
import com.Haris.BudgetTracker.dto.RegisterRequest
import com.Haris.BudgetTracker.model.User
import com.Haris.BudgetTracker.repository.UserRepository
import com.Haris.BudgetTracker.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("User with this email already exists")
        }

        val user = User(
            name = request.name,
            email = request.email,
            passwordHash = requireNotNull(passwordEncoder.encode(request.password)) {
                "Password encoding failed"
            }
        )

        val savedUser = userRepository.save(user)

        val userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(savedUser.email)
            .password(savedUser.passwordHash)
            .authorities("ROLE_USER")
            .build()

        val token = jwtService.generateToken(userDetails, savedUser.userId, savedUser.name)

        return AuthResponse(
            token = token,
            userId = savedUser.userId,
            name = savedUser.name,
            email = savedUser.email,
            message = "Registration successful"
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("User not found") }

        val userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.email)
            .password(user.passwordHash)
            .authorities("ROLE_USER")
            .build()

        val token = jwtService.generateToken(userDetails, user.userId, user.name)

        return AuthResponse(
            token = token,
            userId = user.userId,
            name = user.name,
            email = user.email,
            message = "Login successful"
        )
    }
}