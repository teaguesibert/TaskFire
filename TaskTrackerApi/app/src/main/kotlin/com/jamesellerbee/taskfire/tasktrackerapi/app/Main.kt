package com.jamesellerbee.taskfire.tasktrackerapi.app

import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.account.AccountManager
import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.account.accountRoutes
import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.task.taskRoutes
import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.session.SessionManager
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.TaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.RegistrationStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("main")
    logger.info("Starting up")

    val serviceLocator = ServiceLocator.instance

    val accountManager = AccountManager()
    val sessionManager = SessionManager()
    val taskRepository = TaskRepository()

    serviceLocator.register(
        RegistrationStrategy.Singleton(
            type = AccountManager::class,
            service = accountManager
        )
    )

    serviceLocator.register(
        RegistrationStrategy.Singleton(
            type = SessionManager::class,
            service = sessionManager
        )
    )

    serviceLocator.register(
        RegistrationStrategy.Singleton(
            type = TaskRepository::class,
            service = taskRepository
        )
    )

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

                accountRoutes()
                taskRoutes()
            }
        }
    ).start(wait = true)
}