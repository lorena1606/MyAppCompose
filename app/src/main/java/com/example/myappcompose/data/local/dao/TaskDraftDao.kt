package com.example.myappcompose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myappcompose.data.local.entity.TaskDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDraftDao {
    @Query("SELECT * FROM task_drafts WHERE ownerId = :userId ORDER BY savedAt DESC")
    fun getDraftsByUserId(userId: String): Flow<List<TaskDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDraft(draft: TaskDraftEntity): Long

    @Query("DELETE FROM task_drafts WHERE id = :id")
    fun deleteDraftById(id: Int): Int

    @Query("SELECT * FROM task_drafts WHERE id = :id")
    fun getDraftById(id: Int): TaskDraftEntity?
}
