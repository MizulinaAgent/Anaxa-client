package com.example.anaxa.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val isRegister: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
) {
    val canSubmit: Boolean
        get() = email.isNotBlank() && password.length >= 6 &&
            (!isRegister || username.isNotBlank())
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(AuthUiState())
        private set

    fun onEmailChange(value: String) { state = state.copy(email = value, error = null) }
    fun onPasswordChange(value: String) { state = state.copy(password = value, error = null) }
    fun onUsernameChange(value: String) { state = state.copy(username = value, error = null) }

    fun setRegisterMode(register: Boolean) {
        state = state.copy(isRegister = register, error = null)
    }

    fun submit() {
        val current = state
        if (current.isLoading || !current.canSubmit) return
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = if (current.isRegister) {
                authRepository.register(current.email.trim(), current.password, current.username.trim())
            } else {
                authRepository.login(current.email.trim(), current.password)
            }
            state = result.fold(
                onSuccess = { state.copy(isLoading = false, isAuthenticated = true) },
                onFailure = { state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }
}
