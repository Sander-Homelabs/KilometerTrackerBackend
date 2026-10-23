package database.trip

import database.group.GroupsTable
import database.user.UsersTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TripsTable: Table("trips") {
    val groupId = uuid("group_id").references(GroupsTable.id)
    val start = integer("start")
    val end = integer("end")
    val email = varchar("email", 254).references(UsersTable.email)
    val registeredBy = varchar("registered_by", 254).references(UsersTable.email)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(groupId, start)
}