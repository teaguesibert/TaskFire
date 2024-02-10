package com.jamesellerbee.taskfire.tasktrackerapi.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.account.accountRoutes
import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.task.taskRoutes
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties.ApplicationProperties
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account.ExposedAccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account.InMemoryAccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.task.ExposedTaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.task.InMemoryTaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.AccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.TaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.RegistrationStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val parser = ArgParser("tasktrackerapi")

    val propertiesPath by parser.option(
        type = ArgType.String,
        fullName = "propertiesPath",
        shortName = "p",
        description = "Specify path to find application properties file."
    ).default("./trackerApi.properties")

    val inMemory by parser.option(
        type = ArgType.Boolean,
        fullName = "inMemory",
        description = "Use in memory repositories",
    ).default(false)

    parser.parse(args)

    val logger = LoggerFactory.getLogger("main")
    logger.info("Starting up")

    val serviceLocator = ServiceLocator.instance

    val applicationProperties = ApplicationProperties(propertiesPath)

    serviceLocator.register(
        RegistrationStrategy.Singleton(
            type = ApplicationProperties::class,
            service = applicationProperties
        )
    )

    val accountRepository: AccountRepository = if (inMemory) {
        InMemoryAccountRepository()
    } else {
        ExposedAccountRepository(serviceLocator)
    }


    val taskRepository: TaskRepository = if (inMemory) {
        InMemoryTaskRepository()
    } else {
        ExposedTaskRepository(serviceLocator)
    }

    serviceLocator.register(
        RegistrationStrategy.Singleton(
            type = AccountRepository::class,
            service = accountRepository
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

            install(CORS) {
                anyHost()

                allowHeader(HttpHeaders.ContentType)
                allowHeader(HttpHeaders.AccessControlAllowOrigin)
                allowHeader(HttpHeaders.Authorization)

                allowMethod(HttpMethod.Get)
                allowMethod(HttpMethod.Post)
                allowMethod(HttpMethod.Options)
            }

            install(Authentication) {
                jwt("auth-jwt") {
                    verifier(
                        JWT.require(Algorithm.HMAC256("secret"))
                            .withIssuer("https://0.0.0.0:8080")
                            .withAudience("https://0.0.0.0:8080")
                            .build()
                    )

                    validate { credential ->
                        if (credential.payload.getClaim("accountId").asString() != "") {
                            JWTPrincipal(credential.payload)
                        } else {
                            null
                        }
                    }
                }
            }

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