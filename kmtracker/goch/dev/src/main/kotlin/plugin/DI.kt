package plugin

import app.user.UserHandler
import app.user.UserRepository
import app.user.UserService
import io.ktor.server.application.*
import io.ktor.server.plugins.di.*
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase

fun Application.configureDependencyInjection() {
    dependencies {
        provide<UserHandler> { UserHandler(service = resolve<UserService>()) }
        provide<UserService> { UserService(repository = resolve<UserRepository>()) }
        provide<UserRepository> { UserRepository(database = resolve<R2dbcDatabase>()) }
    }
}
