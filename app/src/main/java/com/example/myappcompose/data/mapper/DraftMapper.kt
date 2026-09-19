package com.example.myappcompose.data.mapper

import com.example.myappcompose.data.local.entity.TaskDraftEntity
import com.example.myappcompose.domain.model.TaskDraft

fun TaskDraftEntity.toDomain(): TaskDraft {
    return TaskDraft(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        savedAt = savedAt
    )
}

fun TaskDraft.toEntity(): TaskDraftEntity {
    return TaskDraftEntity(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        savedAt = savedAt
    )
}
