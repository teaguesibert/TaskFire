package com.jamesellerbee.tasktrackerapi.app.bl.session

import com.jamesellerbee.tasktrackerapi.app.dal.entites.Account
import com.jamesellerbee.tasktrackerapi.app.dal.entites.AuthToken

class SessionManager {
    private val _sessions = mutableMapOf<AuthToken, Account>()
    val sessions get() = _sessions.toMap()

    fun addSession(token: AuthToken, account: Account) {
        _sessions[token] = account
    }
}