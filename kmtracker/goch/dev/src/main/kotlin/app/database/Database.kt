package app.database

import io.r2dbc.spi.IsolationLevel
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabaseConfig

fun createDatabase(): R2dbcDatabase {
    val url = System.getenv("DATABASE_URL_API")
        ?: error("DATABASE_URL_API is not set")

    return R2dbcDatabase.connect(
        url = url,
        databaseConfig = R2dbcDatabaseConfig {
            defaultMaxAttempts = 1
            defaultR2dbcIsolationLevel = IsolationLevel.READ_COMMITTED
        }
    )
}
