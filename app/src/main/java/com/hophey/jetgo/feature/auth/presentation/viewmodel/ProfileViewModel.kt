package com.hophey.jetgo.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hophey.jetgo.feature.auth.domain.usecase.CheckIfLoggedUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.LoginUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.LogoutUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ProfileUiState {
    data object Idle : ProfileUiState
    data object Loading : ProfileUiState
    data object Success : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    checkIfLoggedUseCase: CheckIfLoggedUseCase
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = checkIfLoggedUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun onEmailChanged(newValue: String) {
        _email.value = newValue
        _uiState.value = ProfileUiState.Idle
    }
    fun onPasswordChanged(newValue: String) {
        _password.value = newValue
        _uiState.value = ProfileUiState.Idle
    }

    fun login() = launchAuth {
        loginUseCase(_email.value.trim(), _password.value)
    }
    fun register() = launchAuth {
        registerUseCase(_email.value.trim(), _password.value)
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            logoutUseCase()
            _uiState.value = ProfileUiState.Idle
        }
    }

    private fun launchAuth(block: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            block()
                .onSuccess { _uiState.value = ProfileUiState.Success }
                .onFailure { _uiState.value = ProfileUiState.Error(it.message ?: "Error") }
        }
    }
}