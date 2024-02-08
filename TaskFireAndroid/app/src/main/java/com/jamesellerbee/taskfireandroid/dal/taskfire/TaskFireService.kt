package com.jamesellerbee.taskfireandroid.dal.taskfire

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

data class Account(
    val name: String,
    val password: String = "",
    val id: String = "",
)

data class AuthToken(
    val sessionId: String = "",
    val accountId: String = "",
    val timeStamp: Long = 0
)

interface TaskFireService {
    @POST("/register")
    fun register(@Body account: Account): Call<Unit>


    @POST("/auth")
    fun auth(@Body account: Account): Call<AuthToken>
}