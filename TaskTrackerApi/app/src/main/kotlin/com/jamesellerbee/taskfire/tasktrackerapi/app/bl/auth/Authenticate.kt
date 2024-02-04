package com.jamesellerbee.taskfire.tasktrackerapi.app.bl.auth

import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.session.SessionManager
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.AuthToken
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

suspend fun authToPerformUserAction(
    call: ApplicationCall,
    token: AuthToken,
    accountIdEffected: String,
    callback: suspend () -> Unit
) {
    val serviceLocator = ServiceLocator.instance
    val sessionManager = serviceLocator.resolve<SessionManager>(
        ResolutionStrategy.ByType(
            type = SessionManager::class
        )
    )!!

    if (sessionManager.sessions[token] == null) {
        call.respond(HttpStatusCode.Unauthorized)
    }
    else if (accountIdEffected != sessionManager.sessions[token]?.id) {
        call.respond(HttpStatusCode.Unauthorized, "Not authorized to perform action for given user ID.")
    } else {
        callback()
    }
}