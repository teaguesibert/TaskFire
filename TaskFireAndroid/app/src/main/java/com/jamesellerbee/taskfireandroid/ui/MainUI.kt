package com.jamesellerbee.taskfireandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jamesellerbee.taskfireandroid.ui.theme.TaskFireAndroidTheme

enum class Page {
    HOME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI() {
    var selectedPage by remember { mutableStateOf(Page.HOME) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedPage) {
                            Page.HOME -> "Tasks"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(onClick = {
                        selectedPage = Page.HOME
                    }) {
                        Icon(
                            imageVector = if (selectedPage == Page.HOME) {
                                Icons.Filled.Home
                            } else {
                                Icons.Outlined.Home
                            }, contentDescription = "Navigate to home page"
                        )
                    }
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (selectedPage) {
                Page.HOME -> {

                }
            }
        }
    }
}

@Preview
@Composable
fun MainUiPreview() {
    TaskFireAndroidTheme {
        Surface {
            MainUI()
        }
    }
}