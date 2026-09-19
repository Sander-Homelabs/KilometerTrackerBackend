package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQueryAs(email: String?, db: Database, block: suspend () -> T): T =
    newSuspendedTransaction(db = db) {
        if (email != null) {
            exec(
                "SELECT set_config('app.current_email', ?, true)",
                args = listOf(VarCharColumnType() to email)
            )
        }
        block()
    }