package com.jamesellerbee.taskfireandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jamesellerbee.taskfireandroid.ui.home.Dash
import com.jamesellerbee.taskfireandroid.ui.login.Login
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

@Composable
fun App(serviceLocator: ServiceLocator) {
    val appViewModel = remember { AppViewModel(serviceLocator) }

    val authenticated = appViewModel.authenticated.collectAsState().value
    if (!authenticated) {
        Login(
            serviceLocator = serviceLocator,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
    } else {
        Dash()
    }
}