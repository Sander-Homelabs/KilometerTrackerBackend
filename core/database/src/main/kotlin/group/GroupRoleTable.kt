package database.group

import org.jetbrains.exposed.sql.Table

object GroupRoleTable: Table("group_role") {
    val role = varchar("role", 50)

    override val primaryKey = PrimaryKey(role)
}