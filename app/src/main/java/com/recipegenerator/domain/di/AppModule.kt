package com.recipegenerator.domain.di

import android.content.Context
import com.recipegenerator.data.storage.Settings
import com.recipegenerator.domain.util.AppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSettings(@ApplicationContext context: Context) =
        Settings(context)

    @Provides
    @Singleton
    fun provideAppSettings(@ApplicationContext context: Context) =
        AppSettings(context)

}