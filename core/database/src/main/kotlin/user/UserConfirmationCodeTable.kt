package database.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserConfirmationCodeTable: Table("user_confirmation_code") {
    val email = varchar("email", 254).references(UsersTable.email)
    val code = uuid("code")
    val createdAt = timestamp("createdat")

    override val primaryKey = PrimaryKey(email)
}