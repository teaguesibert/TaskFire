package com.jamesellerbee.taskfireandroid.ui.task

import com.jamesellerbee.taskfireandroid.dal.taskfire.Task

sealed class TaskInteraction {
    data class CreateTask(val task: Task) : TaskInteraction()
}