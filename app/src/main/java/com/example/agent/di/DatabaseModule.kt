package com.example.agent.di

import android.content.Context
import androidx.room.Room

import com.example.agent.data.local.db.AppDatabase
import com.example.agent.data.local.db.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "agent_db"
        ).build()
    }

    @Provides
    fun provideMessageDao(
        database: AppDatabase
    ): MessageDao = database.messageDao()
}
