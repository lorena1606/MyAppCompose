package com.example.myappcompose.ui.screen.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappcompose.domain.model.Task
import com.example.myappcompose.domain.usecase.task.DeleteTaskUseCase
import com.example.myappcompose.domain.usecase.task.GetTasksUseCase
import com.example.myappcompose.domain.usecase.task.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .catch { e -> 
                    _uiState.update { it.copy(error = e.message, isLoading = false) } 
                }
                .collect { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false, error = null) }
                }
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase.toggleCompleted(task.id, task.completed)
                .onFailure { e -> 
                    _uiState.update { it.copy(error = e.message) } 
                }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
                .onFailure { e -> 
                    _uiState.update { it.copy(error = e.message) } 
                }
        }
    }
}
