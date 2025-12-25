package com.example.agent.data.network.dto

data class MessageDto(
    val id: Int,
    val thread_id: Int,
    val user_id: Int,
    val agent_id: Int?,
    val body: String,
    val timestamp: String
)
