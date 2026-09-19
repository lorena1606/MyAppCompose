package com.example.myappcompose.domain.usecase.draft

import com.example.myappcompose.domain.repository.DraftRepository
import javax.inject.Inject

class DeleteDraftUseCase @Inject constructor(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(draftId: Int) {
        repository.deleteDraft(draftId)
    }
}
