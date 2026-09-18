package com.example.myappcompose.domain.repository

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun register(email: String, pass: String): Result<Unit>
    fun logout()
    fun getCurrentUserId(): String?
}