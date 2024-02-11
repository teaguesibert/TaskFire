package com.jamesellerbee.taskfireandroid.ui.login

import com.jamesellerbee.taskfireandroid.dal.taskfire.Account
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.util.ResolutionStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    serviceLocator: ServiceLocator,
    private val onSuccessfulRegister: suspend () -> Unit
) {
    private val taskFireApi by serviceLocator.resolveLazy<TaskFireApi>(
        ResolutionStrategy.ByType(
            type = TaskFireApi::class
        )
    )

    private val _message = MutableStateFlow<Pair<Boolean, String>?>(null)
    val message = _message.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy = _busy.asStateFlow()

    fun onInteraction(interaction: LoginInteraction) {
        when (interaction) {
            is LoginInteraction.Login -> {
                _busy.value = true
                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                    t.printStackTrace()
                    _busy.value = false
                    _message.value = Pair(true, "Something went wrong... try again.")
                }) {
                    val response = taskFireApi.taskFireService.auth(
                        Account(
                            interaction.username,
                            interaction.password
                        )
                    ).execute()

                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                _message.value = Pair(false, "Sign in successful!")
                                val authResponse = response.body()!!
                                taskFireApi.setAccountId(authResponse.id)
                                taskFireApi.setAuthToken(authResponse.token)
                            } ?: run {
                                _message.value = Pair(true, "Something unexpected happened.")
                            }
                        }

                        else -> {
                            _message.value = Pair(true, "There was an issue with your sign in.")
                        }
                    }

                    _busy.value = false
                }
            }

            is LoginInteraction.Register -> {
                _busy.value = true
                CoroutineScope(SupervisorJob()).launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
                    _message.value = Pair(true, "Something went wrong... try again.")
                    _busy.value = false
                }) {
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
                            _message.value = Pair(false, "Create account Successful!")
                            onSuccessfulRegister()
                        }

                        else -> {
                            _message.value =
                                Pair(true, "There was an issue with your registration.")

                        }
                    }

                    _busy.value = false
                }
            }
        }
    }
}