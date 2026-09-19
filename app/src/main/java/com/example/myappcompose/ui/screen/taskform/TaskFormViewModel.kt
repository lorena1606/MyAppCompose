package com.example.myappcompose.ui.screen.taskform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappcompose.domain.usecase.task.CreateTaskUseCase
import com.example.myappcompose.domain.usecase.task.GetTaskUseCase
import com.example.myappcompose.domain.usecase.task.UpdateTaskUseCase
import com.example.myappcompose.domain.usecase.draft.SaveDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskFormUiState(
    val title: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String? = savedStateHandle["taskId"]
    private val _uiState = MutableStateFlow(TaskFormUiState())
    val uiState: StateFlow<TaskFormUiState> = _uiState.asStateFlow()

    init {
        taskId?.let { id ->
            loadTask(id)
        }
    }

    private fun loadTask(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = getTaskUseCase(id)
            if (task != null) {
                _uiState.update { 
                    it.copy(
                        title = task.title, 
                        description = task.description,
                        completed = task.completed,
                        isLoading = false 
                    ) 
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "No se encontró la tarea") }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun saveTask() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "El título es obligatorio") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = if (taskId == null) {
                createTaskUseCase(state.title, state.description)
            } else {
                updateTaskUseCase(taskId, state.title, state.description, state.completed)
            }

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al guardar") }
            }
        }
    }

    fun saveAsDraft() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "El título es obligatorio para el borrador") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = saveDraftUseCase(state.title, state.description)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al guardar borrador") }
            }
        }
    }
}
