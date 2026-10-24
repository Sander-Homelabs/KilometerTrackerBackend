package database.trip

import database.group.GroupsTable
import database.user.UsersTable
import org.jetbrains.exposed.sql.Table

object TankRefillTable: Table("tank_refill") {
    val groupId = uuid("group_id").references(GroupsTable.id)
    val start = integer("start")
    val end = integer("end")
    val cost = double("cost")
    val tanker = varchar("tanker", 254).references(UsersTable.email)

    override val primaryKey = PrimaryKey(groupId, start)
}