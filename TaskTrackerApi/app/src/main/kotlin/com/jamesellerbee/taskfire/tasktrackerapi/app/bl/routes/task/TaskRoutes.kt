package com.jamesellerbee.taskfire.tasktrackerapi.app.bl.routes.task

import com.jamesellerbee.taskfire.tasktrackerapi.app.bl.auth.authToPerformUserAction
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.AuthToken
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Task
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.TaskWrapper
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.TaskRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

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
            token = call.receive<AuthToken>(),
            accountIdEffected = accountId
        ) {
            val tasks = taskRepository.getTasks(accountId)
            call.respond(tasks)
        }
    }

    post("/tasks/{accountId}") {
        val accountId = call.parameters["accountId"]
        if (accountId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val taskWrapper = call.receive<TaskWrapper>()

        authToPerformUserAction(
            call = call,
            token = taskWrapper.authToken,
            accountIdEffected = accountId
        ) {
            taskRepository.addTask(accountId, taskWrapper.task)
            call.respond(HttpStatusCode.OK)
        }
    }
}