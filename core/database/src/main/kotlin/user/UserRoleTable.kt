package database.user

import org.jetbrains.exposed.sql.Table

object UserRoleTable: Table("user_role") {
    val role = varchar("role", 50)

    override val primaryKey = PrimaryKey(role)
}