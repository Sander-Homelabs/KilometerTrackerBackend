package database.group

import database.user.UsersTable
import org.jetbrains.exposed.sql.Table

object GroupsTable: Table("groups") {
    val id = uuid("id")
    val name = varchar("name", 50)
    val createdBy = varchar("created_by", 254).references(UsersTable.email)

    override val primaryKey = PrimaryKey(id)
}