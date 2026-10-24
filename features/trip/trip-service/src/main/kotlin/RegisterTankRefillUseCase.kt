import command.RegisterTankRefillCommand
import exception.DatabaseError
import model.TankRefill

class RegisterTankRefillUseCase(private val tripRepo: TripRepository, private val tankRefillRepo: TankRefillRepository) {
    suspend operator fun invoke(command: RegisterTankRefillCommand) {
        val lastTankRefill = tankRefillRepo.findLastByGroup(command.groupId, command.registeredBy)

        val tankRefillDomain = TankRefill(
            groupId = command.groupId,
            start = lastTankRefill?.end ?: 0,
            end = command.end,
            cost = command.cost,
            tanker = command.tanker,
        )
        tankRefillRepo.save(tankRefill = tankRefillDomain, email = command.registeredBy) ?: throw DatabaseError()

        val tankTrips = tripRepo.findFromStart(command.groupId, lastTankRefill?.end ?: 0, command.registeredBy)
    }
}