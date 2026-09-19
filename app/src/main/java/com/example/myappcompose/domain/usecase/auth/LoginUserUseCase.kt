package com.example.myappcompose.domain.usecase.auth

import com.example.myappcompose.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        return repository.login(email, pass)
    }
}
