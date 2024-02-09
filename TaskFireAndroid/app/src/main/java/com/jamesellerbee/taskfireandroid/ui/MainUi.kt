package com.jamesellerbee.taskfireandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jamesellerbee.taskfireandroid.bl.page.PageProvider
import com.jamesellerbee.taskfireandroid.ui.task.TaskPage
import com.jamesellerbee.taskfireandroid.ui.theme.TaskFireAndroidTheme
import com.jamesellerbee.taskfireandroid.util.RegistrationStrategy
import com.jamesellerbee.taskfireandroid.util.ResolutionStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI(serviceLocator: ServiceLocator) {
    val pageProvider =
        serviceLocator.resolve<PageProvider>(ResolutionStrategy.ByType(PageProvider::class))!!

    val selectedPage = pageProvider.selectedPage.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedPage.titleText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    pageProvider.availablePages.forEach { page ->
                        IconButton(onClick = {
                            pageProvider.setSelectedPage(page)
                        }) {
                            Icon(
                                imageVector = if (selectedPage == page) {
                                    page.selectedIcon
                                } else {
                                    page.icon
                                }, contentDescription = "Navigate to page"
                            )
                        }
                    }
                }
            }
        }
    ) {
        Surface(Modifier.padding(it)) {
            selectedPage.content()
        }
    }
}

@Preview
@Composable
fun MainUiPreview() {
    val serviceLocator = ServiceLocator()

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

    TaskFireAndroidTheme {
        Surface {
            MainUI(serviceLocator)
        }
    }
}