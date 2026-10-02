package com.example

import TaskInput
import TaskResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.receive
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger

val tasksDb = CopyOnWriteArrayList<TaskResponse>()
var nextId = AtomicInteger(1)

fun Route.configureRoutes() {
    get("/tasks") {
        call.respond(tasksDb.toList())
    }

    post("/tasks") {
        try {
            val input = call.receive<TaskInput>()
            if(input.title.isBlank()){
                call.respond(HttpStatusCode.BadRequest,"Текст задачи пуст")
                return@post
            }
            val newTask = TaskResponse(nextId.getAndIncrement(),input.title,false)
            tasksDb.add(newTask)
            call.respond(HttpStatusCode.Created,newTask)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
        }
    }

    delete("/tasks/{id}") {

        val taskId = call.parameters["id"]?.toIntOrNull()
        call.respond(HttpStatusCode.NoContent)
        if(taskId==null){
            call.respond(HttpStatusCode.BadRequest,"Отправьте число")
            return@delete
        }
        val index = tasksDb.indexOfFirst { it.id == taskId }

        if (index == -1) {
            call.respond(HttpStatusCode.NotFound, "Task not found")
        } else {
            tasksDb.removeAt(index)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}
