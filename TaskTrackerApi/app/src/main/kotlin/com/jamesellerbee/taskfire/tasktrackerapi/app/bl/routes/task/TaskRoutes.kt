package com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.task

import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.auth.authToPerformUserAction
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.AuthToken
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Task
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.TaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json

fun Routing.taskRoutes() {
    val serviceLocator = ServiceLocator.instance

    val taskRepository = serviceLocator.resolve<TaskRepository>(
        ResolutionStrategy.ByType(
            type = TaskRepository::class
        )
    )!!

    get("/tasks/{accountId}") {
        val accountId = call.parameters["accountId"]
        if (accountId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        authToPerformUserAction(
            call = call,
            token = Json.decodeFromString<AuthToken>(call.request.headers["AuthToken"]!!),
            accountIdEffected = accountId
        ) {
            val tasks = taskRepository.getTasksByAccountId(accountId)
            call.respond(tasks)
        }
    }

    post("/tasks/{accountId}") {
        val accountId = call.parameters["accountId"]
        if (accountId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val task = call.receive<Task>()

        authToPerformUserAction(
            call = call,
            token = Json.decodeFromString<AuthToken>(call.request.headers["AuthToken"]!!),
            accountIdEffected = accountId
        ) {
            taskRepository.addTask(accountId, task)
            call.respond(HttpStatusCode.OK)
        }
    }
}