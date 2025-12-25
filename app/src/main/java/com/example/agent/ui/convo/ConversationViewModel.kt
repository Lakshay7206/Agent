package com.example.agent.ui.convo


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.domain.repo.AuthRepository
import com.example.agent.domain.repo.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val threadId: Int = checkNotNull(savedStateHandle["threadId"])

    private var currentToken: String? = null

    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState

    init {
        viewModelScope.launch {
            authRepository.observeToken()
                .filterNotNull()
                .onEach { token ->
                    currentToken = token
                }
                .flatMapLatest {
                    messagesRepository.observeMessages()
                }
                .collect { allMessages ->
                    val threadMessages = allMessages
                        .filter { it.thread_id == threadId }
                        .sortedBy { Instant.parse(it.timestamp) }

                    _uiState.update {
                        it.copy(
                            messages = threadMessages,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(input = text) }
    }

    fun sendMessage() {
        val token = currentToken ?: return
        val text = _uiState.value.input.trim()
        if (text.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            messagesRepository.sendMessage(
                token = token,
                threadId = threadId,
                body = text
            )

            _uiState.update {
                it.copy(
                    input = "",
                    isLoading = false
                )
            }

        }
    }
}
