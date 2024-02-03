package com.jamesellerbee.tasktrackerapi.app.dal.entites

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val sessionId: String = "",
    val accountId: String = "",
    val timeStamp: Long = 0
)
