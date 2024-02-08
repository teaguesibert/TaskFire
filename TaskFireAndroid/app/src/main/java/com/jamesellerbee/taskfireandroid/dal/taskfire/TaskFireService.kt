package com.jamesellerbee.taskfireandroid.dal.taskfire

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

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

data class Task(
    val title: String,
    val accountId: String,
    val created: Long,
    val modified: Long = 0,
    val completed: Boolean,
    val description: String = "",
    val taskId: String = ""
)

interface TaskFireService {
    @POST("/register")
    fun register(@Body account: Account): Call<Unit>


    @POST("/auth")
    fun auth(@Body account: Account): Call<AuthToken>

    @GET("/tasks/{accountId}")
    fun getTasks(@Path("accountId") accountId: String): Call<List<Task>>
}