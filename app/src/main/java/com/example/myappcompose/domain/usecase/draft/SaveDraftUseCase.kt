package com.example.myappcompose.domain.usecase.draft

import com.example.myappcompose.domain.model.TaskDraft
import com.example.myappcompose.domain.repository.AuthRepository
import com.example.myappcompose.domain.repository.DraftRepository
import javax.inject.Inject

class SaveDraftUseCase @Inject constructor(
    private val repository: DraftRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(title: String, description: String): Result<Unit> {
        val userId = authRepository.getCurrentUserId() ?: return Result.failure(IllegalStateException("Usuario no autenticado"))
        
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }

        val draft = TaskDraft(
            ownerId = userId,
            title = title,
            description = description,
            savedAt = System.currentTimeMillis()
        )
        
        return try {
            repository.saveDraft(draft)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
