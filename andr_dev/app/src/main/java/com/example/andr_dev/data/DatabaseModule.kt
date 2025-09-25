package com.example.andr_dev.data

import android.content.Context
import androidx.room.Room
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "bugs_database"
        ).build()
    }

    @Provides
    fun providePlayerDao(appDatabase: AppDatabase): PlayerDao {
        return appDatabase.playerDao()
    }

    @Provides
    fun provideScoreDao(appDatabase: AppDatabase): ScoreDao {
        return appDatabase.scoreDao()
    }
}