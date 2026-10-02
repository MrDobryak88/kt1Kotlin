package com.example

import io.ktor.server.application.*
// Импорты для плагинов
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
fun Application.module() {
    println("Application module loaded!")

    // 1. Установка ContentNegotiation для JSON
    install(ContentNegotiation) {
        json()
    }

    // 2. Установка StatusPages для обработки ошибок
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "Error: ${cause.message}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    // 3. Маршрутизация
    routing {
        get("/") {
            call.respondText("Server is running via EngineMain + YAML!")
        }

        // Вызов функции из Routing.kt
        // Убедись, что в Routing.kt эта функция объявлена как extension fun Route.configureRoutes()
        configureRoutes()
    }
}