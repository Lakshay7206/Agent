package com.example.agent.di
import com.example.agent.data.local.dataStore.TokenDataStore
import com.example.agent.data.local.db.MessageDao
import com.example.agent.domain.repo.AuthRepository
import com.example.agent.domain.repo.AuthRepositoryImpl
import com.example.agent.domain.repo.MessagesRepository
import com.example.agent.domain.repo.MessagesRepositoryImpl
import com.greedygame.brokenandroidcomposeproject.network.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(
        api: ApiService,
        tokenStore: TokenDataStore
    ): AuthRepository =
        AuthRepositoryImpl(api, tokenStore)

    @Provides
    fun provideMessagesRepository(
        api: ApiService,
        dao: MessageDao
    ): MessagesRepository =
        MessagesRepositoryImpl(
            api,
            dao
        )
}
