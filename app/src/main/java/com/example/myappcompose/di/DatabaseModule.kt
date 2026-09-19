package com.example.myappcompose.di

import android.content.Context
import androidx.room.Room
import com.example.myappcompose.data.local.dao.TaskDraftDao
import com.example.myappcompose.data.local.database.AppDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "task_manager_db"
        ).build()
    }

    @Provides
    fun provideTaskDraftDao(database: AppDatabase): TaskDraftDao {
        return database.taskDraftDao()
    }
}
