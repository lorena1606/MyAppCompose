package com.example.myappcompose.domain.usecase.task

import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Task? {
        return repository.getTaskById(taskId)
    }
}
