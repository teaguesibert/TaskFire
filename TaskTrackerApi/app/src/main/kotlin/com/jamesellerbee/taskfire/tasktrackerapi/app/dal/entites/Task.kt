package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites

import kotlinx.serialization.Serializable

@Serializable
data class TaskWrapper(
    val authToken: AuthToken,
    val task: Task
)

@Serializable
data class Task(
    val title: String,
    val accountId: String ,
    val created: Long,
    val modified: Long = 0,
    val completed: Boolean,
    val description: String = "",
    val taskId: String = ""
)
