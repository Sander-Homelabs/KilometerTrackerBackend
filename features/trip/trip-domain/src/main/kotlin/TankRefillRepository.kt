import model.TankRefill
import java.util.UUID

interface TankRefillRepository {
    suspend fun findByGroup(groupId: UUID, email: String): List<TankRefill>
    suspend fun findLastByGroup(groupId: UUID, email: String): TankRefill?
    suspend fun save(tankRefill: TankRefill, email: String): TankRefill?
}