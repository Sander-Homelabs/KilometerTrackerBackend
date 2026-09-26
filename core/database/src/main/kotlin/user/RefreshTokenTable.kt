package database.user

import org.jetbrains.exposed.sql.Table

object RefreshTokenTable: Table("refresh_token") {
    val email = varchar("email", 254).references(UsersTable.email)
    val token = text("token")
    val active = bool("active").default(true)

    override val primaryKey = PrimaryKey(email, token)
}