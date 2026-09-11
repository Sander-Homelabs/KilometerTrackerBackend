package plugin

import app.database.createDatabase
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig
import io.r2dbc.spi.IsolationLevel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

fun Application.configureExposed() {
    val database = createDatabase()

    dependencies {
        provide<R2dbcDatabase> {
            database
        }
    }
}
