package di

import ActivateUserUseCase
import RegisterUserUseCase
import org.koin.dsl.module

val userServiceModule = module {
    single<RegisterUserUseCase> { RegisterUserUseCase(get(), get(), get()) }
    single<ActivateUserUseCase> { ActivateUserUseCase(get(), get(), get(), get())}
}