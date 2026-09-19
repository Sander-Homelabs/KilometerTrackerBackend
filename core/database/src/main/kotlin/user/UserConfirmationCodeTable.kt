package database.user

import org.jetbrains.exposed.sql.Table

object UserConfirmationCodeTable: Table("user_confirmation_code") {
    val email = varchar("email", 254).references(UsersTable.email)
    val code = uuid("code")

    override val primaryKey = PrimaryKey(email)
}