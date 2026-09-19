package com.example.myappcompose.domain.usecase.auth

import com.example.myappcompose.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): String? {
        return repository.getCurrentUserId()
    }
}
