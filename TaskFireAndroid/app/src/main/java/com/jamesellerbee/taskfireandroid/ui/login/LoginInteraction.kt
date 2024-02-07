package com.jamesellerbee.taskfireandroid.ui.login

sealed class LoginInteraction {
    data class Login(val username: String, val password: String) : LoginInteraction()
    data class Register(val username: String, val password: String) : LoginInteraction()

}