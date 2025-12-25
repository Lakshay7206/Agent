package com.example.agent.domain.repo

import com.example.agent.data.local.db.MessageDao
import com.example.agent.data.local.db.toDto
import com.example.agent.data.local.db.toEntity
import com.example.agent.data.network.dto.MessageDto
import com.example.agent.data.network.dto.SendMessageRequest
import com.greedygame.brokenandroidcomposeproject.network.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MessagesRepository {
    suspend fun observeMessages(): Flow<List<MessageDto>>
    suspend fun refreshMessages(token: String)
    suspend fun sendMessage(
        token: String,
        threadId: Int,
        body: String
    ): MessageDto
}
class MessagesRepositoryImpl(
    private val api: ApiService,
    private val dao: MessageDao
) : MessagesRepository {

    override suspend fun observeMessages(): Flow<List<MessageDto>> {
        val cached=dao.observeMessages().map { it.map { e -> e.toDto() } }
        return cached


    }

    override suspend fun refreshMessages(token: String) {
        val network = api.getMessages(token)
        dao.clearAll()
        dao.insertAll(network.map { it.toEntity() })
    }

    override suspend fun sendMessage(
        token: String,
        threadId: Int,
        body: String
    ): MessageDto {
        val sent = api.sendMessage(
            token,
            SendMessageRequest(threadId, body)
        )

        dao.insertAll(listOf(sent.toEntity()))
        return sent
    }

}

