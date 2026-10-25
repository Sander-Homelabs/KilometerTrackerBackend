import command.RegisterTankRefillCommand
import exception.DatabaseError
import model.TankRefill
import kotlin.math.round

class RegisterTankRefillUseCase(
    private val tripRepo: TripRepository,
    private val tankRefillRepo: TankRefillRepository,
    private val emailService: EmailService,
) {
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
            .ifEmpty { throw DatabaseError() }
        val userKilometers = tankTrips.groupBy { it.email }.mapValues { (_, trips) -> trips.sumOf { it.end - it.start } }

        val costPerKilometer = command.cost / (tankRefillDomain.end - tankRefillDomain.start)

        emailService.sendEmail(
            EmailRequest.TankRefillNotification(
                recipientEmail = userKilometers.keys.toList().plus(tankRefillDomain.tanker).distinct(),
                costPerKilometer = costPerKilometer,
                start = tankRefillDomain.start,
                end = tankRefillDomain.end,
                tanker = tankRefillDomain.tanker,
                totalCost = tankRefillDomain.cost,
                costs = userKilometers.map {
                    TankRefillCosts(cost = round(it.value * costPerKilometer * 100) / 100, user = it.key)
                }
            )
        )
    }
}