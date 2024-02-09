package com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.account

import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.session.SessionManager
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Account
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.AuthToken
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.AccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.util.UUID
import kotlinx.serialization.json.Json

/**
 * Routes related to accounts.
 *
 */
fun Routing.accountRoutes() {
    val serviceLocator = ServiceLocator.instance

    val accountRepository = serviceLocator.resolve<AccountRepository>(
        ResolutionStrategy.ByType(type = AccountRepository::class)
    )!!

    val sessionManager = serviceLocator.resolve<SessionManager>(
        ResolutionStrategy.ByType(type = SessionManager::class)
    )!!

    get("/accounts") {
        val token = Json.decodeFromString<AuthToken>(call.request.headers["AuthToken"]!!)

        if (sessionManager.sessions[token] == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        val message = if (call.request.queryParameters["name"] == null) {
            accountRepository.getAccounts().map { it.copy(password = "") }
        } else {
            accountRepository.getAccounts()
                .filter { it.name == call.request.queryParameters["name"] }
                .map { it.copy(password = "") }
        }

        call.respond(message)
    }

    post("/auth") {
        val account = call.receive<Account>()

        val existingAccount =
            accountRepository.getAccounts().firstOrNull {
                it.name == account.name
                        && it.password == account.password
            }

        if (existingAccount != null) {
            val auth = AuthToken(UUID.randomUUID().toString(), existingAccount.id, System.currentTimeMillis())

            // Store session, overwriting any previous sessions for this account
            sessionManager.addSession(auth, existingAccount)
            call.respond(auth)

        } else {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }

    post("/register") {
        val newAccount = call.receive<Account>()

        if (newAccount.password.isBlank()) {
            call.respond(HttpStatusCode.NotAcceptable, "Password cannot be blank")
        }

        if (accountRepository.getAccounts().none { account ->
                account.name == newAccount.name
            }) {
            accountRepository.addAccount(newAccount.copy(id = UUID.randomUUID().toString()))
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Conflict, "Account already exists with that name")
        }
    }
}