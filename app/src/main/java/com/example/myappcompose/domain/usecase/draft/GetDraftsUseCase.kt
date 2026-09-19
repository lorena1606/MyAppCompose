package com.example.myappcompose.domain.usecase.draft

import com.example.myappcompose.domain.model.TaskDraft
import com.example.myappcompose.domain.repository.AuthRepository
import com.example.myappcompose.domain.repository.DraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class GetDraftsUseCase @Inject constructor(
    private val repository: DraftRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<List<TaskDraft>> {
        val userId = authRepository.getCurrentUserId() ?: return emptyFlow()
        return repository.getDrafts(userId)
    }
}
