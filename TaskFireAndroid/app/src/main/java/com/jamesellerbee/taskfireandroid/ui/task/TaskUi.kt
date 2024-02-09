package com.jamesellerbee.taskfireandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jamesellerbee.taskfireandroid.dal.taskfire.Task
import com.jamesellerbee.taskfireandroid.util.ServiceLocator
import kotlinx.coroutines.launch

sealed class FormAction {
    data class Submit(val account: Task) : FormAction()
    data object Cancel : FormAction()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Task(serviceLocator: ServiceLocator) {
    val viewModel = remember { TaskViewModel(serviceLocator) }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            TaskForm {
                when (it) {
                    FormAction.Cancel -> {}

                    is FormAction.Submit -> {
                        viewModel.onInteraction(TaskInteraction.CreateTask(it.account))
                    }
                }

                scope.launch {
                    scaffoldState.bottomSheetState.hide()
                }
            }
        }) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add task")
                }
            }
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                Text("Tasks")
                val tasks = viewModel.tasks.collectAsState().value
                if (tasks.isEmpty()) {
                    Text("There are no tasks. Getting started by adding some.")
                } else {
                    tasks.forEach { task ->
                        Text(text = task.toString())
                    }
                }
            }
        }
    }
}


@Composable
fun TaskForm(onFormAction: (FormAction) -> Unit) {
    Column(modifier = Modifier.padding(4.dp)) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = { Text("Title") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                title = ""
                description = ""

                onFormAction(
                    FormAction.Submit(
                        Task(
                            title = title,
                            accountId = description,
                            created = System.currentTimeMillis(),
                        )
                    )
                )
            }) {
                Text("Submit")
            }

            Button(onClick = {
                title = ""
                description = ""

                onFormAction(FormAction.Cancel)
            }) {
                Text("Cancel")
            }
        }
    }
}