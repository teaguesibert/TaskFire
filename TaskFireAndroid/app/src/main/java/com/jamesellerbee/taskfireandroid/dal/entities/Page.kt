package com.jamesellerbee.taskfireandroid.dal.entities

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

interface Page {
    val icon: ImageVector
    val selectedIcon: ImageVector
    val titleText: String
    val content: @Composable () -> Unit
}