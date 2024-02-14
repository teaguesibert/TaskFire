package com.jamesellerbee.taskfireandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jamesellerbee.taskfireandroid.bl.page.PageProvider
import com.jamesellerbee.taskfireandroid.dal.settings.AppSettings
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.ui.App
import com.jamesellerbee.taskfireandroid.ui.task.TaskPage
import com.jamesellerbee.taskfireandroid.ui.theme.TaskFireAndroidTheme
import com.jamesellerbee.taskfireandroid.util.RegistrationStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceLocator = ServiceLocator.instance

        val appSettings = AppSettings(this)

        serviceLocator.register(
            RegistrationStrategy.Singleton(
                type = AppSettings::class,
                service = appSettings
            )
        )

        val taskFireApi = TaskFireApi("https://taskfireapi.jamesellerbee.com")

        serviceLocator.register(
            RegistrationStrategy.Singleton(
                type = TaskFireApi::class,
                service = taskFireApi
            )
        )

        val initialPage = TaskPage(serviceLocator)
        val pageProvider = PageProvider(
            initialPage = initialPage,
            availablePages = listOf(
                initialPage
            )
        )

        serviceLocator.register(
            RegistrationStrategy.Singleton(
                type = PageProvider::class,
                service = pageProvider
            )
        )

        setContent {
            TaskFireAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(serviceLocator)
                }
            }
        }
    }
}