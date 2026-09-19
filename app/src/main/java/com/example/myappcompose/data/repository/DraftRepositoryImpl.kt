package com.example.myappcompose.data.repository

import com.example.myappcompose.data.local.dao.TaskDraftDao
import com.example.myappcompose.data.mapper.toDomain
import com.example.myappcompose.data.mapper.toEntity
import com.example.myappcompose.domain.model.TaskDraft
import com.example.myappcompose.domain.repository.DraftRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftRepositoryImpl @Inject constructor(
    private val taskDraftDao: TaskDraftDao
) : DraftRepository {

    override fun getDrafts(ownerId: String): Flow<List<TaskDraft>> {
        return taskDraftDao.getDraftsByUserId(ownerId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveDraft(draft: TaskDraft) = withContext(Dispatchers.IO) {
        taskDraftDao.insertDraft(draft.toEntity())
        Unit
    }

    override suspend fun deleteDraft(id: Int) = withContext(Dispatchers.IO) {
        taskDraftDao.deleteDraftById(id)
        Unit
    }

    override suspend fun getDraftById(id: Int): TaskDraft? = withContext(Dispatchers.IO) {
        taskDraftDao.getDraftById(id)?.toDomain()
    }
}
