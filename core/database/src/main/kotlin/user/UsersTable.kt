package database.user

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val email = varchar("email", 254)
    val status = varchar("status", 50).references(UserStatusTable.status)
    val firstName = varchar("firstname", 50).nullable()
    val lastName = varchar("lastname", 50).nullable()
    val role = varchar("role", 50).references(UserRoleTable.role)

    override val primaryKey = PrimaryKey(email)
}