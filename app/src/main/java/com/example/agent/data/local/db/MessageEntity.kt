package com.example.agent.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.agent.data.network.dto.MessageDto

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int,
    val threadId: Int,
    val userId: Int,
    val agentId: Int?,
    val body: String,
    val timestamp: String
)
fun MessageDto.toEntity() = MessageEntity(
    id = id,
    threadId = thread_id,
    userId = user_id,
    agentId = agent_id,
    body = body,
    timestamp = timestamp
)

fun MessageEntity.toDto() = MessageDto(
    id = id,
    thread_id = threadId,
    user_id = userId,
    agent_id = agentId,
    body = body,
    timestamp = timestamp
)

