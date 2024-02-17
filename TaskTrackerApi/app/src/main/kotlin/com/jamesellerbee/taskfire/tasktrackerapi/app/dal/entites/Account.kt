package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val name: String,
    val password: String = "",
    val id: String = "",
)