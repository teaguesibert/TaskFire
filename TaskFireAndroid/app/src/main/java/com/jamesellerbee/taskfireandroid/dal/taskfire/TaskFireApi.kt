package com.jamesellerbee.taskfireandroid.dal.taskfire

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import java.util.concurrent.TimeUnit

class TaskFireApi(baseUrl: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val taskFireService = retrofit.create<TaskFireService>()

    private val _authenticated = MutableStateFlow(false)
    val authenticated = _authenticated.asStateFlow()

    private var authToken: AuthToken? = null

    fun setAuthToken(token: AuthToken) {
        authToken = token
        _authenticated.value = true
    }
}