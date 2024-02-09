package com.jamesellerbee.taskfireandroid.ui.task

import com.jamesellerbee.taskfireandroid.dal.taskfire.Task

sealed class TaskInteraction {
    data class UpsertTask(val task: Task) : TaskInteraction()
}