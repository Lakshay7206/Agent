package com.example.agent.ui.thread

data class ThreadsUiState(
    val threads: List<ThreadUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
data class ThreadUiModel(
    val threadId: Int,
    val lastMessage: String,
    val timestamp: String,
    val sender: String
)
