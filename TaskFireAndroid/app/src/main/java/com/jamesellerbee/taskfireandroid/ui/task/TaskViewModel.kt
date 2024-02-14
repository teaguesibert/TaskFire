package com.jamesellerbee.taskfireandroid.ui.task

import com.google.gson.Gson
import com.jamesellerbee.taskfireandroid.dal.taskfire.Task
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.util.ResolutionStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(serviceLocator: ServiceLocator) {
    private val taskFireApi by serviceLocator.resolveLazy<TaskFireApi>(
        ResolutionStrategy.ByType(
            type = TaskFireApi::class
        )
    )

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }) {
            refreshList()
        }
    }

    fun onInteraction(interaction: TaskInteraction) {
        when (interaction) {
            is TaskInteraction.UpsertTask -> {
                CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                }) {
                    taskFireApi.taskFireService.createTask(
                        authToken = taskFireApi.authToken,
                        accountId = taskFireApi.accountId,
                        task = interaction.task
                    ).execute()

                    refreshList()
                }
            }

            is TaskInteraction.DeleteTask -> {
                CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                }) {
                    taskFireApi.taskFireService.deleteTask(
                        authToken = taskFireApi.authToken,
                        accountId = taskFireApi.accountId,
                        taskId = interaction.task.taskId
                    ).execute()

                    refreshList()
                }
            }

            is TaskInteraction.Refresh -> {
                CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                }) {
                    refreshList()
                    interaction.onRefreshComplete()
                }
            }
        }
    }

    private fun refreshList() {
        val response = taskFireApi.taskFireService.getTasks(
            authToken = taskFireApi.authToken,
            accountId = taskFireApi.accountId,
        ).execute()

        if (response.isSuccessful) {
            _tasks.value = response.body() ?: emptyList()
        }
    }
}