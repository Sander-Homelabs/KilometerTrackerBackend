package database.group

import GroupRepository
import database.dbQueryAs
import model.Group
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.UUID

class GroupRepositoryImpl(private val db: Database): GroupRepository {
    override suspend fun findById(id: UUID, email: String): Group? =
        dbQueryAs(email, db) {
            GroupsTable.selectAll()
                .where { GroupsTable.id eq id }
                .map { it.toDomain() }
                .singleOrNull()
        }

    override suspend fun findByEmail(email: String): List<Group> =
        dbQueryAs(email, db) {
            GroupsTable.selectAll()
                .map { it.toDomain() }
        }

    override suspend fun save(name: String, email: String): Group? =
        dbQueryAs(email, db) {
            GroupsTable.insertReturning {
                it[GroupsTable.id] = UUID.randomUUID()
                it[GroupsTable.name] = name
                it[GroupsTable.createdBy] = email
            }.singleOrNull()?.toDomain()
        }

    override suspend fun update(id: UUID, name: String, email: String) {
        dbQueryAs(email, db) {
            GroupsTable.update({ GroupsTable.id eq id }) {
                it[GroupsTable.name] = name
            }
        }
    }

    override suspend fun deactivate(id: UUID, email: String) {
        dbQueryAs(email, db) {
            GroupsTable.update({ GroupsTable.id eq id }) {
                it[GroupsTable.active] = false
            }
        }
    }

    private fun ResultRow.toDomain() = Group(
        id = this[GroupsTable.id],
        name = this[GroupsTable.name],
        createdBy = this[GroupsTable.createdBy]
    )
}