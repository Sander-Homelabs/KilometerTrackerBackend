package di

import DeleteTripUseCase
import RegisterTankRefillUseCase
import RegisterTripUseCase
import org.koin.dsl.module

val tripServiceModule = module {
    single<RegisterTripUseCase> { RegisterTripUseCase(get()) }
    single<RegisterTankRefillUseCase> { RegisterTankRefillUseCase(get(), get(), get()) }
    single<DeleteTripUseCase> { DeleteTripUseCase(get(), get()) }
}