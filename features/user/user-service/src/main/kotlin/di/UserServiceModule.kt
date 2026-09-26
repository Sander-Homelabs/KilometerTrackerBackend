package di

import ActivateUserUseCase
import LoginUseCase
import RefreshAccessTokenUseCase
import RegisterUserUseCase
import org.koin.dsl.module

val userServiceModule = module {
    single<RegisterUserUseCase> { RegisterUserUseCase(get(), get(), get()) }
    single<ActivateUserUseCase> { ActivateUserUseCase(get(), get(), get(), get()) }
    single<LoginUseCase> { LoginUseCase(get(), get(), get(), get()) }
    single<RefreshAccessTokenUseCase> { RefreshAccessTokenUseCase(get()) }
}