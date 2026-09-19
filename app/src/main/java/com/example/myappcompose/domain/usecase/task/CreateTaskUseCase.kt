package com.example.myappcompose.domain.usecase.task

import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(title: String, description: String): Result<String> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("El título no puede estar vacío"))
        }
        
        val currentTime = System.currentTimeMillis()
        val task = Task(
            title = title,
            description = description,
            completed = false,
            createdAt = currentTime,
            updatedAt = currentTime
        )
        
        return try {
            val id = repository.addTask(task)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
