import command.DeleteTripCommand
import exception.DatabaseError
import exception.IllegalDeletion
import extension.isNotNull

class DeleteTripUseCase(
    private val tripRepo: TripRepository,
    private val tankRefillRepo: TankRefillRepository
) {
    suspend operator fun invoke(command: DeleteTripCommand) {
        val tankRefill = tankRefillRepo.findLastByGroup(command.groupId, command.deletedBy)
        val lastTrip = tripRepo.findLastByGroup(command.groupId, command.deletedBy)

        if ((lastTrip?.start ?: 0) != command.start) throw IllegalDeletion()

        if (tankRefill.isNotNull() && tankRefill.end == lastTrip?.end) {
            val deletedTankRefill = tankRefillRepo.delete(command.groupId, tankRefill.start, command.deletedBy)
            if (deletedTankRefill < 1) throw DatabaseError()
        }

        val deletedTrip = tripRepo.delete(command.groupId, command.start, command.deletedBy)
        if (deletedTrip < 1) throw DatabaseError()
    }
}