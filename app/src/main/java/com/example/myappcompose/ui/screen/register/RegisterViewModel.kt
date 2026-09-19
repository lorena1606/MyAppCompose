package com.example.myappcompose.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myappcompose.domain.usecase.auth.RegisterUserUseCase
import com.example.myappcompose.ui.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun register(email: String, pass: String, confirmPass: String) {
        if (_state.value.isLoading) return

        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            _state.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(error = "Formato de correo inválido") }
            return
        }

        if (pass.length < 6) {
            _state.update { it.copy(error = "La contraseña debe tener al menos 6 caracteres") }
            return
        }

        if (pass != confirmPass) {
            _state.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUserUseCase(email, pass)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error al registrarse") }
            }
        }
    }

    fun resetSuccess() {
        _state.update { it.copy(isSuccess = false) }
    }
}
