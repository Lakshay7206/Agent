package com.greedygame.brokenandroidcomposeproject.network.api


import com.example.agent.data.network.dto.LoginRequest
import com.example.agent.data.network.dto.LoginResponse
import com.example.agent.data.network.dto.MessageDto
import com.example.agent.data.network.dto.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("api/messages")
    suspend fun getMessages(
        @Header("X-Branch-Auth-Token") token: String
    ): List<MessageDto>

    @POST("api/messages")
    suspend fun sendMessage(
        @Header("X-Branch-Auth-Token") token: String,
        @Body body: SendMessageRequest
    ): MessageDto

    @POST("api/reset")
    suspend fun reset(
        @Header("X-Branch-Auth-Token") token: String
    )
}
