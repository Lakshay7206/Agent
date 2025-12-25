package com.example.agent.ui.thread

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agent.domain.repo.AuthRepository
import com.example.agent.domain.repo.MessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


@HiltViewModel
class ThreadsViewModel @Inject constructor(
    private val repo: MessagesRepository,
    authRepository: AuthRepository
) : ViewModel() {

    private var token: String? = null
    private var initialFetchDone = false
    val threads: StateFlow<List<ThreadUiModel>> =
        authRepository.observeToken()
            .filterNotNull()
            .flatMapLatest { token ->
                repo.observeMessages()
                    .onEach { messages ->
                        // 🔑 If DB empty → fetch from API ONCE
                        if (messages.isEmpty()) {
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
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun refresh() {
        val t = token ?: return
        viewModelScope.launch {
            repo.refreshMessages(t)
        }
    }
}

