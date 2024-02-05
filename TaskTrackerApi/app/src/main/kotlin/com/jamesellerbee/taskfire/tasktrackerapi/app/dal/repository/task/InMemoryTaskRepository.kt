package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.task

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Task
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.TaskRepository

class InMemoryTaskRepository : TaskRepository {
    private val tasks = mutableMapOf<String, MutableList<Task>>()

    override fun getTasksByAccountId(accountId: String): List<Task> {
        return tasks[accountId] ?: emptyList()
    }

    override fun addTask(accountId: String, task: Task) {
        tasks.computeIfAbsent(accountId) { mutableListOf() }.add(task)
    }

    override fun getTasks(): List<Task> {
        return tasks.values.flatten().toList()
    }
}