package database.user

import org.jetbrains.exposed.sql.Table

object UserStatusTable: Table("user_status") {
    val status = varchar("status", 50)

    override val primaryKey = PrimaryKey(status)
}