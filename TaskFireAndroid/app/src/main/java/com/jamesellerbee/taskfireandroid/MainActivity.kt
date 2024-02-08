package com.jamesellerbee.taskfireandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.ui.App
import com.jamesellerbee.taskfireandroid.ui.theme.TaskFireAndroidTheme
import com.jamesellerbee.taskfireandroid.util.RegistrationStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceLocator = ServiceLocator.instance
        val taskFireApi = TaskFireApi("http://192.168.1.68:8080")

        serviceLocator.register(
            RegistrationStrategy.Singleton(
                type = TaskFireApi::class,
                taskFireApi
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