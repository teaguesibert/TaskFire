package com.jamesellerbee.taskfireandroid.dal.taskfire

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class TaskFireApi(baseUrl: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val taskFireService = retrofit.create<TaskFireService>()

    private val _authenticated = MutableStateFlow(false)
    val authenticated = _authenticated.asStateFlow()

    private var _authToken: AuthToken? = null
    val authToken get() = _authToken!!

    fun setAuthToken(token: AuthToken) {
        _authToken = token
        _authenticated.value = true
    }
}