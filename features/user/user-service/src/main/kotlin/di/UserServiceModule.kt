package di

import RegisterUserUseCase
import org.koin.dsl.module

val userServiceModule = module {
    single<RegisterUserUseCase> { RegisterUserUseCase(get(), get(), get()) }
}