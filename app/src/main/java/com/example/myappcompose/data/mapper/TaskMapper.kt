package com.example.myappcompose.data.mapper

import com.example.myappcompose.data.remote.model.TaskDocument
import com.example.myappcompose.domain.model.Task

fun TaskDocument.toDomain(): Task {
    return Task(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toDocument(): TaskDocument {
    return TaskDocument(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
