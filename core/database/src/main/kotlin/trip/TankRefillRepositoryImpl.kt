package database.trip

import TankRefillRepository
import database.dbQueryAs
import model.TankRefill
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class TankRefillRepositoryImpl(private val db: Database): TankRefillRepository {
    override suspend fun findByGroup(groupId: UUID, email: String): List<TankRefill> =
        dbQueryAs(email, db) {
            TankRefillTable
                .selectAll()
                .where { TankRefillTable.groupId eq groupId }
                .orderBy(TankRefillTable.start, SortOrder.DESC)
                .limit(10)
                .map { it.toDomain() }
        }

    override suspend fun findLastByGroup(groupId: UUID, email: String): TankRefill? =
        findByGroup(groupId, email).firstOrNull()

    override suspend fun save(tankRefill: TankRefill, email: String): TankRefill? =
        dbQueryAs(email, db) {
            TankRefillTable.insertReturning {
                it[TankRefillTable.groupId] = tankRefill.groupId
                it[TankRefillTable.start] = tankRefill.start
                it[TankRefillTable.end] = tankRefill.end
                it[TankRefillTable.cost] = tankRefill.cost
                it[TankRefillTable.tanker] = tankRefill.tanker
            }.singleOrNull()?.toDomain()
        }

    private fun ResultRow.toDomain() = TankRefill(
        groupId = this[TankRefillTable.groupId],
        start = this[TankRefillTable.start],
        end = this[TankRefillTable.end],
        cost = this[TankRefillTable.cost],
        tanker = this[TankRefillTable.tanker]
    )
}