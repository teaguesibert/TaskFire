package com.jamesellerbee.taskfireandroid.dal.taskfire

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

data class Task(
    val title: String,
    val created: Long,
    val accountId: String = "",
    val description: String = "",
    val modified: Long = 0,
    val completed: Boolean = false,
    val taskId: String = ""
)

data class AuthResponse(
    val token: String,
    val id: String
)

interface TaskFireService {
    @POST("/register")
    fun register(@Body account: Account): Call<Unit>

    @POST("/auth")
    fun auth(@Body account: Account): Call<AuthResponse>

    @GET("/tasks/{accountId}")
    fun getTasks(
        @Header("Authorization") authToken: String,
        @Path("accountId") accountId: String
    ): Call<List<Task>>

    @POST("/tasks/{accountId}")
    fun createTask(
        @Header("Authorization") authToken: String,
        @Path("accountId") accountId: String,
        @Body task: Task
    ): Call<Unit>

    @DELETE("/tasks/{accountId}/{taskId}")
    fun deleteTask(
        @Header("Authorization") authToken: String,
        @Path("accountId") accountId: String,
        @Path("taskId") taskId: String
    ): Call<Unit>
}