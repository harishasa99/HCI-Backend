package com.Haris.BudgetTracker.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String,

    @Value("\${app.jwt.expiration}")
    private val jwtExpiration: Long
) {

    private fun getSigningKey(): SecretKey {
        return Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateToken(userDetails: UserDetails, userId: Long, name: String): String {
        return Jwts.builder()
            .subject(userDetails.username)
            .claim("userId", userId)
            .claim("name", name)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey())
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractUserId(token: String): Long {
        val claims = extractAllClaims(token)
        val userIdValue = claims["userId"]
        return when (userIdValue) {
            is Int -> userIdValue.toLong()
            is Long -> userIdValue
            is String -> userIdValue.toLong()
            else -> throw IllegalArgumentException("Invalid userId in token")
        }
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }
}