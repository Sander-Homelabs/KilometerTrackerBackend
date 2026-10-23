package di

import RegisterTripUseCase
import org.koin.dsl.module

val tripServiceModule = module {
    single<RegisterTripUseCase> { RegisterTripUseCase(get()) }
}