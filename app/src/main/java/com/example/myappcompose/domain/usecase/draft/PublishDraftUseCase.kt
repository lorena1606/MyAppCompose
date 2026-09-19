package com.example.myappcompose.domain.usecase.draft

import com.example.myappcompose.domain.repository.DraftRepository
import com.example.myappcompose.domain.usecase.task.CreateTaskUseCase
import javax.inject.Inject

class PublishDraftUseCase @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val repository: DraftRepository
) {
    suspend operator fun invoke(draftId: Int): Result<Unit> {
        val draft = repository.getDraftById(draftId) 
            ?: return Result.failure(IllegalArgumentException("No se encontró el borrador"))

        // 1. Intentar publicar en Firebase
        val result = createTaskUseCase(draft.title, draft.description)
        
        return result.mapCatching {
            // 2. Si tiene éxito, eliminar de Room
            deleteDraftUseCase(draftId)
        }
        // Si result es Failure, se propaga el error y el borrador permanece en Room
    }
}
