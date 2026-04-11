package com.Haris.BudgetTracker.service

import com.Haris.BudgetTracker.dto.CreateUserRequest
import com.Haris.BudgetTracker.model.User
import com.Haris.BudgetTracker.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(request: CreateUserRequest): User {
        val user = User(
            name = request.name,
            email = request.email,
            passwordHash = request.password
        )

        return userRepository.save(user)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}