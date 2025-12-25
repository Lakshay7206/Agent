package com.example.agent.ui.convo

import com.example.agent.data.network.dto.MessageDto

data class ConversationUiState(
    val messages: List<MessageDto> = emptyList(),
    val input: String = "",
    val isLoading: Boolean = false
)
