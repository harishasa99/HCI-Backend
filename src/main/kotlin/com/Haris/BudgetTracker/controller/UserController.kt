package com.Haris.BudgetTracker.controller

import com.Haris.BudgetTracker.dto.CreateUserRequest
import com.Haris.BudgetTracker.model.User
import com.Haris.BudgetTracker.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): User {
        return userService.createUser(request)
    }

    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }
}