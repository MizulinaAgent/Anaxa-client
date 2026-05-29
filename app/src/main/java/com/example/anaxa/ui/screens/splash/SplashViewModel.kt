package com.example.anaxa.ui.screens.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthState { CHECKING, AUTHENTICATED, UNAUTHENTICATED }

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var authState by mutableStateOf(AuthState.CHECKING)
        private set

    init {
        viewModelScope.launch {
            val hasToken = authRepository.isLoggedIn.first()
            authState = if (hasToken && authRepository.getMe().isSuccess) {
                AuthState.AUTHENTICATED
            } else {
                if (hasToken) authRepository.logout()
                AuthState.UNAUTHENTICATED
            }
        }
    }
}
