package com.example.myappcompose.domain.repository

import com.example.myappcompose.domain.model.TaskDraft
import kotlinx.coroutines.flow.Flow

interface DraftRepository {
    fun getDrafts(ownerId: String): Flow<List<TaskDraft>>
    suspend fun saveDraft(draft: TaskDraft)
    suspend fun deleteDraft(id: Int)
    suspend fun getDraftById(id: Int): TaskDraft?
}
