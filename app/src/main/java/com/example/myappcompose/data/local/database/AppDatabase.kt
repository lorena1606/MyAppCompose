package com.example.myappcompose.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myappcompose.data.local.dao.TaskDraftDao
import com.example.myappcompose.data.local.entity.TaskDraftEntity

@Database(entities = [TaskDraftEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDraftDao(): TaskDraftDao
}
