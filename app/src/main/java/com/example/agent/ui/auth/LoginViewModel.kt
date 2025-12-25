package com.example.agent.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.domain.repo.AuthRepository
import com.example.agent.ui.utility.AuthErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    val authToken = authRepository.observeToken()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }


    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.login(_uiState.value.email, _uiState.value.password)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()
                        ?.message
                        ?.let { raw -> AuthErrorMapper.map(raw) }
                )
            }
        }
    }
}
