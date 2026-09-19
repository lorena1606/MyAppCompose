package com.example.myappcompose.ui.screen.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappcompose.domain.model.TaskDraft
import com.example.myappcompose.domain.usecase.draft.DeleteDraftUseCase
import com.example.myappcompose.domain.usecase.draft.GetDraftsUseCase
import com.example.myappcompose.domain.usecase.draft.PublishDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DraftsUiState(
    val drafts: List<TaskDraft> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isPublishing: Boolean = false
)

@HiltViewModel
class DraftsViewModel @Inject constructor(
    private val getDraftsUseCase: GetDraftsUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val publishDraftUseCase: PublishDraftUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DraftsUiState())
    val uiState: StateFlow<DraftsUiState> = _uiState.asStateFlow()

    init {
        observeDrafts()
    }

    private fun observeDrafts() {
        viewModelScope.launch {
            getDraftsUseCase()
                .catch { e -> _uiState.update { it.copy(error = e.message, isLoading = false) } }
                .collect { drafts ->
                    _uiState.update { it.copy(drafts = drafts, isLoading = false, error = null) }
                }
        }
    }

    fun deleteDraft(draftId: Int) {
        viewModelScope.launch {
            deleteDraftUseCase(draftId)
        }
    }

    fun publishDraft(draftId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isPublishing = true, error = null) }
            publishDraftUseCase(draftId)
                .onSuccess {
                    _uiState.update { it.copy(isPublishing = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isPublishing = false, error = e.message ?: "Error al publicar") }
                }
        }
    }
}
