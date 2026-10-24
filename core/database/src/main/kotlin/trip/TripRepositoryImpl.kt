package database.trip

import TripRepository
import database.dbQueryAs
import model.Trip
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class TripRepositoryImpl(private val db: Database): TripRepository {
    override suspend fun findByGroup(groupId: UUID, user: String): List<Trip> =
        dbQueryAs(user, db) {
            TripsTable
                .selectAll()
                .where { TripsTable.groupId eq groupId }
                .orderBy(TripsTable.start, SortOrder.DESC)
                .limit(10)
                .map { it.toDomain() }
        }

    override suspend fun findByStart(groupId: UUID, start: Int, user: String): Trip? =
        dbQueryAs(user, db) {
            TripsTable
                .selectAll()
                .where { (TripsTable.groupId eq groupId) and (TripsTable.start eq start) }
                .singleOrNull()
                ?.toDomain()
        }

    override suspend fun findLastByGroup(groupId: UUID, user: String): Trip? =
        findByGroup(groupId, user).firstOrNull()

    override suspend fun save(trip: Trip, user: String): Trip? =
        dbQueryAs(user, db) {
            TripsTable.insertReturning {
                it[TripsTable.groupId] = trip.groupId
                it[TripsTable.start] = trip.start
                it[TripsTable.end] = trip.end
                it[TripsTable.email] = trip.email
            }.singleOrNull()?.toDomain()
        }

    override suspend fun delete(groupId: UUID, start: Int, user: String): Int =
        dbQueryAs(user, db) {
            TripsTable.deleteWhere {
                (TripsTable.groupId eq groupId) and (TripsTable.start eq start)
            }
        }

    private fun ResultRow.toDomain() = Trip(
        groupId = this[TripsTable.groupId],
        start = this[TripsTable.start],
        end = this[TripsTable.end],
        email = this[TripsTable.email]
    )
}