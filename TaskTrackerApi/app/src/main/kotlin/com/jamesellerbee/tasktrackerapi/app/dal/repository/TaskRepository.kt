package com.jamesellerbee.tasktrackerapi.app.dal.repository

import com.jamesellerbee.tasktrackerapi.app.dal.entites.Task

class TaskRepository {
    private val tasks = mutableMapOf<String, MutableList<Task>>()

    fun addTask(accountId: String, task: Task) {
        tasks.computeIfAbsent(accountId) { mutableListOf() }.add(task)
    }

    fun getTasks(accountId: String): List<Task> {
        return tasks[accountId]?.toList() ?: emptyList()
    }
}