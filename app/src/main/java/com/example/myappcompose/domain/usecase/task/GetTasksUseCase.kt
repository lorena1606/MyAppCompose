package com.example.myappcompose.domain.usecase.task

import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.observeTasks()
    }
}
