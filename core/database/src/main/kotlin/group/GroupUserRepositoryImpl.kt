package database.group

import database.dbQueryAs
import GroupUserRepository
import model.GroupRole
import model.GroupUser
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class GroupUserRepositoryImpl(private val db: Database) : GroupUserRepository {
    override suspend fun findByGroupId(groupId: UUID, email: String): List<GroupUser> =
        dbQueryAs(email, db) {
            GroupUserTable.selectAll()
                .where { GroupUserTable.groupId eq groupId }
                .map { it.toDomain() }
        }

    override suspend fun save(email: String, groupId: UUID, userEmail: String, role: GroupRole): GroupUser? =
        dbQueryAs(email, db) {
            GroupUserTable.insert {
                it[GroupUserTable.groupId] = groupId
                it[GroupUserTable.email] = userEmail
                it[GroupUserTable.role] = role.dbValue
            }.resultedValues?.singleOrNull()?.toDomain()
        }

    private fun ResultRow.toDomain(): GroupUser = GroupUser(
        groupId = this[GroupUserTable.groupId],
        email = this[GroupUserTable.email],
        role = GroupRole.fromDbValue(this[GroupUserTable.role])
    )
}