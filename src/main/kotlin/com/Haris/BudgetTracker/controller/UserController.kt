package com.Haris.BudgetTracker.controller

import com.Haris.BudgetTracker.dto.CreateUserRequest
import com.Haris.BudgetTracker.dto.UserResponse
import com.Haris.BudgetTracker.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody request: CreateUserRequest): UserResponse {
        return userService.createUser(request)
    }

    @GetMapping
    fun getAllUsers(): List<UserResponse> {
        return userService.getAllUsers()
    }
    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): UserResponse {
        return userService.getUserById(userId)
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable userId: Long) {
        userService.deleteUser(userId)
    }
}