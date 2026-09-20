package app

import ActivateUserUseCase
import RegisterUserUseCase
import database.di.databaseModule
import di.emailModule
import di.userServiceModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import plugins.configureErrorHandling
import userRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureErrorHandling()

    install(Koin) {
        slf4jLogger()
        modules(databaseModule, emailModule, userServiceModule)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        userRoutes(register = get<RegisterUserUseCase>(), activate = get<ActivateUserUseCase>())
    }
}