package com.example.agent.data.network.dto

data class SendMessageRequest(
    val thread_id: Int,
    val body: String
)
