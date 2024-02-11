package com.jamesellerbee.taskfireandroid.ui.task

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jamesellerbee.taskfireandroid.dal.taskfire.Task
import com.jamesellerbee.taskfireandroid.util.ServiceLocator
import com.jamesellerbee.taskfireandroid.util.toDateString
import com.jamesellerbee.taskfireandroid.util.toDateTimeString
import com.jamesellerbee.taskfireandroid.util.toTimeString
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

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            TaskForm {
                when (it) {
                    FormAction.Cancel -> {}

                    is FormAction.Submit -> {
                        viewModel.onInteraction(TaskInteraction.UpsertTask(it.account))
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
            Surface(Modifier.padding(paddingValues)) {
                Column {
                    if (selectedTask == null) {
                        val tasks = viewModel.tasks.collectAsState().value
                        if (tasks.isEmpty()) {
                            Text("There are no tasks. Getting started by adding some.")
                        } else {
                            LazyColumn(Modifier.padding(top = 12.dp)) {
                                items(tasks) { task ->
                                    Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                                        TaskCard(task, viewModel) {
                                            selectedTask = it
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        TaskDetails(selectedTask = selectedTask!!)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskCard(
    task: Task,
    viewModel: TaskViewModel,
    onTaskCardClicked: (Task) -> Unit
) {
    var showTaskActionsDropdown by remember { mutableStateOf(false) }

    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .combinedClickable(
                    onClick = {
                        onTaskCardClicked(task)
                    },
                    onLongClick = {
                        showTaskActionsDropdown = true
                    }
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = {
                        viewModel.onInteraction(
                            TaskInteraction.UpsertTask(
                                task.copy(completed = !task.completed)
                            )
                        )
                    },
                )

                Text(
                    text = task.title,
                    textDecoration = if (task.completed) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    },
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = task.modified.toDateString().plus(","),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = task.modified.toTimeString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            DropdownMenu(
                expanded = showTaskActionsDropdown,
                onDismissRequest = { showTaskActionsDropdown = false }) {
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        viewModel.onInteraction(TaskInteraction.DeleteTask(task))
                        showTaskActionsDropdown = false
                    })
            }
        }
    }
}

@Composable
fun TaskDetails(selectedTask: Task) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Title",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.weight(1f))

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit title"
            )
        }
    }
    Text(text = selectedTask.title)

    if (selectedTask.description.isNotBlank()) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = selectedTask.description)
    }

    Text(
        text = "Created on",
        style = MaterialTheme.typography.headlineSmall
    )
    Text(text = selectedTask.created.toDateTimeString())
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
                onFormAction(
                    FormAction.Submit(
                        Task(
                            title = title,
                            description = description,
                            created = System.currentTimeMillis(),
                        )
                    )
                )

                title = ""
                description = ""
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