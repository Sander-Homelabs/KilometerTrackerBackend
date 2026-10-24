import model.Trip
import java.util.UUID

interface TripRepository {
    suspend fun findByGroup(groupId: UUID, user: String): List<Trip>
    suspend fun findByStart(groupId: UUID, start: Int, user: String): Trip?
    suspend fun findLastByGroup(groupId: UUID, user: String): Trip?
    suspend fun save(trip: Trip, user: String): Trip?
    suspend fun delete(groupId: UUID, start: Int, user: String): Int
}