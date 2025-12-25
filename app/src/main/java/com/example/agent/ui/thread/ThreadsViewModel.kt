package com.example.agent.ui.thread

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.domain.repo.AuthRepository
import com.example.agent.domain.repo.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val repo: MessagesRepository,
    authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThreadsUiState())
    val uiState: StateFlow<ThreadsUiState> = _uiState.asStateFlow()

    private var token: String? = null
    private var initialFetchDone = false

    init {
        authRepository.observeToken()
            .filterNotNull()
            .onEach { token = it }
            .flatMapLatest { token ->
                repo.observeMessages()
                    .onStart {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    .onEach { messages ->
                        // 🔑 Fetch from API ONCE if DB empty
                        if (messages.isEmpty() && !initialFetchDone) {
                            initialFetchDone = true
                            repo.refreshMessages(token)
                        }
                    }
            }
            .map { messages ->
                messages
                    .groupBy { it.thread_id }
                    .map { (_, msgs) ->
                        val last = msgs.maxBy { Instant.parse(it.timestamp) }
                        ThreadUiModel(
                            threadId = last.thread_id,
                            lastMessage = last.body,
                            timestamp = last.timestamp,
                            sender = if (last.agent_id == null) "Customer" else "Agent"
                        )
                    }
                    .sortedByDescending { Instant.parse(it.timestamp) }
            }
            .onEach { threads ->
                _uiState.update {
                    it.copy(
                        threads = threads,
                        isLoading = false
                    )
                }
            }
            .catch { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        val t = token ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repo.refreshMessages(t)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}

