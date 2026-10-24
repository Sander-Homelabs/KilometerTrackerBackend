import command.RegisterTankRefillCommand

class RegisterTankRefillUseCase(private val tripRepo: TripRepository, private val tankRefillRepo: TankRefillRepository) {
    suspend operator fun invoke(command: RegisterTankRefillCommand) {

    }
}