import command.RegisterTripCommand
import exception.DatabaseError
import exception.InvalidTripStart
import model.Trip

class RegisterTripUseCase(private val tripRepo: TripRepository) {
    suspend operator fun invoke(command: RegisterTripCommand) {
        if (command.start > command.end) throw InvalidTripStart()

        val previousTrip = tripRepo.findLastByGroup(command.groupId, command.registeredBy)
        if (command.start != (previousTrip?.end ?: 0)) throw InvalidTripStart()

        val domainTrip = Trip(command.groupId, command.start, command.end, command.email)
        tripRepo.save(domainTrip, command.registeredBy) ?: throw DatabaseError()
    }
}