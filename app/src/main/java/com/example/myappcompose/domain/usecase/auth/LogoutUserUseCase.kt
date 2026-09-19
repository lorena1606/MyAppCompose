package com.example.myappcompose.domain.usecase.auth

import com.example.myappcompose.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() {
        repository.logout()
    }
}
