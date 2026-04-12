package com.Haris.BudgetTracker.service

import com.Haris.BudgetTracker.dto.CreateUserRequest
import com.Haris.BudgetTracker.dto.UserResponse
import com.Haris.BudgetTracker.model.User
import com.Haris.BudgetTracker.repository.UserRepository
import org.springframework.stereotype.Service
import com.Haris.BudgetTracker.exception.ResourceNotFoundException

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(request: CreateUserRequest): UserResponse {
        val user = User(
            name = request.name,
            email = request.email,
            passwordHash = request.password
        )

        val savedUser = userRepository.save(user)
        return mapToUserResponse(savedUser)
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { mapToUserResponse(it) }
    }

    private fun mapToUserResponse(user: User): UserResponse {
        return UserResponse(
            userId = user.userId,
            name = user.name,
            email = user.email
        )
    }
    fun getUserById(userId: Long): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with id $userId not found") }

        return mapToUserResponse(user)
    }

    fun deleteUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with id $userId not found") }

        userRepository.delete(user)
    }
}