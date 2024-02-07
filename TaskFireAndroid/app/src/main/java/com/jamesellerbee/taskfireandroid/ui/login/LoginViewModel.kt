package com.jamesellerbee.taskfireandroid.ui.login

import com.jamesellerbee.taskfireandroid.dal.taskfire.Account
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.util.ResolutionStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(serviceLocator: ServiceLocator) {
    private val taskFireApi by serviceLocator.resolveLazy<TaskFireApi>(
        ResolutionStrategy.ByType(
            type = TaskFireApi::class
        )
    )

    private val _message = MutableStateFlow<Pair<Boolean, String>?>(null)
    val message = _message.asStateFlow()

    fun onInteraction(interaction: LoginInteraction) {
        when (interaction) {
            is LoginInteraction.Login -> {
                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                    val response = taskFireApi.taskFireService.auth(
                        Account(
                            interaction.username,
                            interaction.password
                        )
                    ).execute()

                    when (response.code()) {
                        200 -> {
                            _message.value = Pair(false, "Success.")
                            taskFireApi.authToken = response.body()
                        }

                        else -> {
                            _message.value = Pair(true, "There was an issue with your sign in.")
                        }
                    }
                }
            }

            is LoginInteraction.Register -> {
                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                    val response = taskFireApi.taskFireService.register(
                        Account(
                            interaction.username,
                            interaction.password
                        )
                    ).execute()

                    when (response.code()) {
                        409 -> {
                            _message.value =
                                Pair(true, "There already exists an account with this name")
                        }

                        200 -> {
                            _message.value = Pair(false, "Success!")
                        }
                    }
                }
            }
        }
    }
}