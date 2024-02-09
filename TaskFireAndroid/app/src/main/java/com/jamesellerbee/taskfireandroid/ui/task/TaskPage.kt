package com.jamesellerbee.taskfireandroid.ui.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.jamesellerbee.taskfireandroid.dal.entities.Page
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

class TaskPage(private val serviceLocator: ServiceLocator) : Page {
    override val icon: ImageVector
        get() = Icons.Outlined.Home
    override val selectedIcon: ImageVector
        get() = Icons.Filled.Home
    override val titleText: String
        get() = "Tasks"

    override val content: @Composable () -> Unit
        get() = {
            Task(serviceLocator)
        }
}