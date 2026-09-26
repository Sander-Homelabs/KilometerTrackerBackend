package database.security

import database.user.UsersTable
import org.jetbrains.exposed.sql.Table

object UserPasswordTable: Table("user_password") {
    val email = varchar("email", 254).references(UsersTable.email)
    val password = text("password")
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(email, password)
}