package com.jamesellerbee.taskfireandroid.ui.login

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jamesellerbee.taskfireandroid.R
import com.jamesellerbee.taskfireandroid.ui.theme.TaskFireAndroidTheme
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

@Composable
fun Login(serviceLocator: ServiceLocator, modifier: Modifier = Modifier) {
    val loginViewModel = remember { LoginViewModel(serviceLocator) }

    val message = loginViewModel.message.collectAsState().value

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.temp_logo),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "TaskFire",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,

                ),
            modifier = Modifier.padding(8.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                message?.let {
                    Text(
                        text = it.second,
                        color = if (it.first) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    label = { Text(text = "Username") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = { Text(text = "Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {

                    }) {
                        Text(text = "Sign in")
                    }

                    Button(onClick = {
                        loginViewModel.onInteraction(
                            LoginInteraction.Register(
                                username = username,
                                password = password
                            )
                        )
                    }) {
                        Text("Create an account")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    val serviceLocator = ServiceLocator()

    TaskFireAndroidTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Login(serviceLocator = serviceLocator, modifier = Modifier.fillMaxSize())
        }
    }
}