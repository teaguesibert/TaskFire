package com.jamesellerbee.tasktrackerapi.app

import com.jamesellerbee.tasktrackerapi.app.bl.account.AccountManager
import com.jamesellerbee.tasktrackerapi.app.dal.entites.Account
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.util.UUID
import kotlinx.serialization.json.JsonObject
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("main")
    logger.info("Starting up")
    val accountManager = AccountManager()

    embeddedServer(
        factory = Netty,
        port = 8080,
        module = {
            install(ContentNegotiation) {
                json()
            }

            install(CallLogging)

            routing {
                get("/") {
                    call.respondText("Hello, world!")
                }

                get("/accounts") {
                    val account =
                        accountManager.accounts.values.firstOrNull { it.name == call.request.queryParameters["name"] }

                    call.respond(account ?: JsonObject(emptyMap()))
                }

                post("/register") {
                    val newAccount = call.receive<Account>()
                    if (accountManager.accounts.none { (_, account) ->
                            account.name == newAccount.name
                        }) {
                        accountManager.addAccount(newAccount.copy(id = UUID.randomUUID().toString()))
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Conflict, "Account already exists with that name")
                    }

                }
            }
        }
    ).start(wait = true)
}