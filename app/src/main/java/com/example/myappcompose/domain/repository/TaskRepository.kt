package com.example.myappcompose.domain.repository

import com.example.myappcompose.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(taskId: String): Task?
    suspend fun getCompletedTasks(): List<Task>
    fun observeTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task): String
    suspend fun updateTask(taskId: String, changes: Map<String, Any>)
    suspend fun deleteTask(taskId: String)
}
