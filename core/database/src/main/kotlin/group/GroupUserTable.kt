package database.group

import database.user.UsersTable
import org.jetbrains.exposed.sql.Table

object GroupUserTable: Table("group_user") {
    val email = varchar("email", 254).references(UsersTable.email)
    val groupId = uuid("group_id").references(GroupsTable.id)
    val role = varchar("role", 50).references(GroupRoleTable.role).default("MEMBER")

    override val primaryKey = PrimaryKey(email, groupId)
}