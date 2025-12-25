package com.yourapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.agent.data.local.dataStore.TokenDataStore
import com.example.agent.data.local.dataStore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideTokenDataStore(
        dataStore: DataStore<Preferences>
    ): TokenDataStore =
        TokenDataStore(dataStore)
}
