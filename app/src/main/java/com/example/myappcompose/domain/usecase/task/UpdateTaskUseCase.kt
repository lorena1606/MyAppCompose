package com.example.myappcompose.domain.usecase.task

import com.example.myappcompose.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, title: String, description: String, completed: Boolean): Result<Unit> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }

        val changes = mutableMapOf<String, Any>(
            "title" to title,
            "description" to description,
            "completed" to completed,
            "updatedAt" to System.currentTimeMillis()
        )

        return try {
            repository.updateTask(taskId, changes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleCompleted(taskId: String, currentStatus: Boolean): Result<Unit> {
        val changes = mapOf<String, Any>(
            "completed" to !currentStatus,
            "updatedAt" to System.currentTimeMillis()
        )
        return try {
            repository.updateTask(taskId, changes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
