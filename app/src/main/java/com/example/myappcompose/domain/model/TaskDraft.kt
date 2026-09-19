package com.example.myappcompose.domain.model

data class TaskDraft(
    val id: Int = 0,
    val ownerId: String,
    val title: String,
    val description: String,
    val savedAt: Long
)
